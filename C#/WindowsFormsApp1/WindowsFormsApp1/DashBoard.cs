using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Windows.Forms.DataVisualization.Charting;

namespace WindowsFormsApp1 {
    public partial class DashBoard : UserControl {
        DateTime nowDate = DateTime.Now;
        Entity entity = new Entity();
        Color[] colors = { Color.DodgerBlue, Color.SteelBlue, Color.Chocolate, Color.ForestGreen };
        List<Panel> panels = new List<Panel>();
        public DashBoard() {
            InitializeComponent();
            string[] str = "오늘 티켓 판매,누적 매출,진행공연,배치 부스".Split(',');
            string[] values = "982 매c\\ 45,820,000c1 / 10c5 / 50".Split('c');
            string s1 = entity.TicketType.Select(t => t.Sold).Sum() + " 매";
            string s2 = "\\ " + entity.TicketType.Select(t => t.Sold * t.Price).Sum();
            string s3 = entity.EventItem.ToList().Where(t => {
                string[] strs = t.EndHour.ToString().Split('.');
                double m = (double.Parse(strs[1]) / 100) * 60;
                new TimeSpan(int.Parse(strs[0]), (int) m, 0);
                return false;
            }).ToList().Count + " / " + entity.EventItem.ToList().Count;
            string s4 = entity.Booth.ToList().Count + " / 50";
            int i = 0;
            for (int j = 0; j < str.Length; j++) {
                string s = str[j];
                Panel p = new Panel();
                p.BackColor = Color.White;
                p.Size = new Size(190, 80);
                p.BorderStyle = BorderStyle.FixedSingle;

                Panel colorP  = new Panel() {
                    Dock = DockStyle.Left,
                    Width = 5,
                    BackColor = colors[j]
                };

                Panel panel = new Panel() {
                    BackColor = Color.White,
                    Dock = DockStyle.Fill,
                    Padding = new Padding(15, 15, 15, 15)
                };

                Label lblTitle = new Label {
                    Text = s,
                    ForeColor = Color.Gray,
                    Font = new Font("맑은 고딕", 9F),
                    AutoSize = true,
                    Location = new Point(15, 12)
                };

                Label lblValue = new Label {
                    Text = values[i],
                    ForeColor = Color.Black,
                    Font = new Font("맑은 고딕", 18F, FontStyle.Bold),
                    AutoSize = true,
                    Location = new Point(15, 35)
                };
                i++;

                panel.Controls.Add(lblTitle);
                panel.Controls.Add(lblValue);
                p.Controls.Add(panel);
                p.Controls.Add(colorP);
                panels.Add(p);
                Controls.Add(p);
            }
            userChart1.ChartTitle = "티켓 종류별 판매량(매)";
            userChart2.ChartTitle = "부스 유형별 배치 수";
            userChart3.ChartTitle = "스테이지별 공연 수";
            userChart4.ChartTitle = "스태프 역할별 인원";
            UserChart[] cs = { userChart1, userChart2, userChart3, userChart4 };
            for(int s = 0; s < cs.Length; s++)
                cs[s].SeriesColor = colors[s];

            List<int> ticekOrder = new List<int> { 2, 4, 3, 1 };
            entity.TicketType.ToList().OrderBy(t => ticekOrder.IndexOf(t.Id)).ToList().ForEach(t => {
                userChart1.AddData(t.Name, t.Sold);
            });
            List<string> boothNames = new List<string>("푸드,게임,안내,스테이지,스폰서,휴게".Split(','));
            entity.BoothType.ToList().OrderBy(t => boothNames.IndexOf(t.Name)).ToList().ForEach(t => {
                userChart2.AddData(t.Name, t.Booth.Count);
            });
;           List<string> stageNames = new List<string>("메인,야외,서브,토크룸".Split(','));
            entity.Stage.ToList().OrderBy(t => stageNames.IndexOf(t.Name)).ToList().ForEach(t => {
                userChart3.AddData(t.Name, t.EventItem.Count);
            });
            List<string> staffs = new List<string>("무대,티켓,안전,운영,미디어".Split(','));
            List<string> staffsJob = new List<string>("stage,ticket,safety,ops,media".Split(','));
            var staffList = entity.Staff.ToList();
            for (int s = 0; s < staffs.Count; s++) {
                userChart4.AddData(staffs[s], staffList.Where(t => staffsJob[s].Equals(t.Role)).ToList().Count);
            }
        }

        private void DashBoard_Load(object sender, EventArgs e) {
            int w = Width;
            int h = Height;
            int gap = 10;
            int hgap = 160;

            for(int i = 0; i < panels.Count; i++) {
                panels[i].Location = new Point(10 + (200 * i), 50);
            }

            userChart1.Location = new Point(gap, hgap);
            userChart1.Size = new Size(w/2 - gap, (h - hgap) /2 - gap);

            userChart2.Location = new Point(w/2 + gap, hgap);
            userChart2.Size = new Size(w / 2 - gap * 2, (h - hgap) / 2 - gap);

            userChart3.Location = new Point(gap, hgap + ((h - hgap) / 2));
            userChart3.Size = new Size(w / 2 - gap, (h - hgap) / 2 - gap);

            userChart4.Location = new Point(w / 2 + gap, hgap + ((h - hgap) / 2));
            userChart4.Size = new Size(w / 2 - gap * 2, (h - hgap) / 2 - gap);
        }
    }
}
