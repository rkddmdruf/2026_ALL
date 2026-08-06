using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;

public class Seat
{
    public int No, Name;
    public Rectangle Rect;
}

public class SeatMapMin : Control
{
    const int W = 380, H = 330;
    readonly Point start = new Point(131, 141);
    readonly Timer timer = new Timer { Interval = 20 };
    readonly ToolTip tip = new ToolTip();
    readonly List<Point> path = new List<Point>(), trail = new List<Point>();
    List<Seat> seats;
    HashSet<int> reserved;
    Bitmap image;
    bool[,] wall;
    Seat selected, next, over;
    Point user;
    int floor, index, state; // 0=정지, 1=이동, -1=복귀

    public int? SeatNo { get { return selected == null ? (int?)null : selected.No; } }
    public event Action<bool> ButtonsChanged; // 결제와 reset을 동시에 활성/비활성

    public SeatMapMin()
    {
        Size = new Size(W, H);
        DoubleBuffered = true;
        user = start;
        trail.Add(start);
        timer.Tick += (s, e) => MoveUser();
    }

    public void SetMap(Image map, IEnumerable<Seat> data,
                       IEnumerable<int> reservedNos, int selectedFloor)
    {
        image = new Bitmap(map, W, H);
        seats = data.ToList();
        reserved = new HashSet<int>(reservedNos);
        floor = selectedFloor;
        MakeWall();
        selected = next = over = null;
        path.Clear(); trail.Clear(); trail.Add(start);
        user = start; state = 0; timer.Stop(); Buttons(false);
        Invalidate();
    }

    protected override void OnPaint(PaintEventArgs e)
    {
        if (image == null) return;
        e.Graphics.DrawImageUnscaled(image, 0, 0);
        List<Point> line = state == 1 ? path : trail;
        if (line.Count > 1)
            using (Pen p = new Pen(Color.DodgerBlue, 2))
                e.Graphics.DrawLines(p, line.ToArray());

        foreach (Seat s in seats)
        {
            Color c = reserved.Contains(s.No) ? Color.Gray :
                      s == selected ? Color.OrangeRed : Color.SaddleBrown;
            using (Brush b = new SolidBrush(c)) e.Graphics.FillRectangle(b, s.Rect);
            e.Graphics.DrawRectangle(Pens.Black, s.Rect);
        }
        e.Graphics.FillEllipse(Brushes.Red, user.X - 4, user.Y - 4, 8, 8);
    }

    protected override void OnMouseMove(MouseEventArgs e)
    {
        Seat s = seats.FirstOrDefault(x => x.Rect.Contains(e.Location));
        if (s == over) return;
        over = s;
        tip.SetToolTip(this, s == null ? "" : Code(s));
    }

    protected override void OnMouseClick(MouseEventArgs e)
    {
        Seat s = seats.FirstOrDefault(x => x.Rect.Contains(e.Location));
        if (s == null || s == selected) return;
        if (reserved.Contains(s.No))
        {
            MessageBox.Show("예약된 좌석입니다.", "경고",
                MessageBoxButtons.OK, MessageBoxIcon.Warning);
            return;
        }

        selected = next = s;
        Buttons(false);
        if (state == -1) return;
        if (trail.Count == 1) Go(s);
        else { state = -1; timer.Start(); }
        Invalidate();
    }

    public void ResetToStart()
    {
        if (state != 0 || trail.Count == 1) return;
        selected = next = null;
        Buttons(false);
        state = -1;
        timer.Start();
    }

    void Go(Seat target)
    {
        next = null;
        path.Clear(); path.AddRange(FindPath(target));
        trail.Clear(); trail.Add(start);
        user = start; index = 1; state = 1;
        timer.Start();
    }

    void MoveUser()
    {
        for (int speed = 0; speed < 4; speed++)
        {
            if (state == 1)
            {
                user = path[index++];
                trail.Add(user);
                if (index == path.Count)
                {
                    timer.Stop(); state = 0; Buttons(true);
                    Invalidate(); Update();
                    MessageBox.Show(Code(selected) + "위치에 도착하였습니다.", "정보",
                        MessageBoxButtons.OK, MessageBoxIcon.Information);
                    break;
                }
            }
            else
            {
                trail.RemoveAt(trail.Count - 1);
                user = trail[trail.Count - 1];
                if (trail.Count == 1)
                {
                    timer.Stop(); state = 0;
                    Seat s = next; next = null;
                    if (s != null) Go(s);
                    break;
                }
            }
        }
        Invalidate();
    }

    List<Point> FindPath(Seat target)
    {
        bool[,] block = (bool[,])wall.Clone();
        foreach (Seat s in seats)
            if (s != target) Fill(block, s.Rect, true, 2);
        Fill(block, target.Rect, false, 2);
        Fill(block, new Rectangle(start.X, start.Y, 1, 1), false, 4);

        Point goal = new Point(target.Rect.Left + target.Rect.Width / 2,
                               target.Rect.Top + target.Rect.Height / 2);
        bool[,] seen = new bool[W, H];
        Point[,] before = new Point[W, H];
        Queue<Point> q = new Queue<Point>();
        q.Enqueue(start); seen[start.X, start.Y] = true;

        int v = start.Y <= goal.Y ? 1 : -1;
        int h = start.X <= goal.X ? 1 : -1;
        Point[] dirs = { new Point(0, v), new Point(h, 0),
                         new Point(-h, 0), new Point(0, -v) };

        while (!seen[goal.X, goal.Y])
        {
            Point p = q.Dequeue();
            foreach (Point d in dirs)
            {
                Point n = new Point(p.X + d.X, p.Y + d.Y);
                if (n.X < 0 || n.Y < 0 || n.X >= W || n.Y >= H ||
                    block[n.X, n.Y] || seen[n.X, n.Y]) continue;
                seen[n.X, n.Y] = true;
                before[n.X, n.Y] = p;
                q.Enqueue(n);
            }
        }

        List<Point> result = new List<Point>();
        for (Point p = goal; ; p = before[p.X, p.Y])
        {
            result.Add(p);
            if (p == start) break;
        }
        result.Reverse();
        return result;
    }

    void MakeWall()
    {
        wall = new bool[W, H];
        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++)
            {
                Color c = image.GetPixel(x, y);
                if (c.R < 100 && c.G < 100 && c.B < 100)
                    Fill(wall, new Rectangle(x, y, 1, 1), true, 2);
            }
    }

    static void Fill(bool[,] a, Rectangle r, bool value, int margin)
    {
        for (int y = Math.Max(0, r.Top - margin); y < Math.Min(H, r.Bottom + margin); y++)
            for (int x = Math.Max(0, r.Left - margin); x < Math.Min(W, r.Right + margin); x++)
                a[x, y] = value;
    }

    string Code(Seat s)
    {
        return (floor == 1 ? "A" : floor == 2 ? "B" : "C") + s.Name.ToString("00");
    }

    void Buttons(bool enabled)
    {
        if (ButtonsChanged != null) ButtonsChanged(enabled);
    }
}

/* 연결 코드
seatMap.ButtonsChanged += v => btnPay.Enabled = btnReset.Enabled = v;
btnReset.Click += (s, e) => seatMap.ResetToStart();
seatMap.SetMap(floorImage, seats, reservedSeatNos, floor);
*/
