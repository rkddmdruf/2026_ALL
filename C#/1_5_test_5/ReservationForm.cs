using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml;

namespace _1_5_test_5 {
    public partial class ReservationForm : Form {
        hotel h;
        public DateTime startTime, endTime;
        public int price;
        public ReservationForm(int hno) {
            h = sp.entity.hotel.FirstOrDefault(t => t.hno == hno);
            InitializeComponent();
            for (int i = 2; i <= 4; i++) {
                var t = (TextBox)Controls["textBox" + i];
                t.ReadOnly = true;
                t.BackColor = Color.White; t.ForeColor = Color.Gray;
            }
            Icon = Properties.Resources.logo;
            titleImage.Image = Properties.Resources.logo.ToBitmap();
        }

        private void ReservationForm_Load(object sender, EventArgs e) {
            pictureBox1.Image = Properties.Resources.ResourceManager.GetObject("_" + h.hno) as Bitmap;
            nameLabel.Text = h.hName;
            textBox1.BackColor = Color.White;

            gradeLabel.Text = "등급 : " + h.ratno + "성급(" + new string('★', h.ratno.Value) + ")";
            addressLabel.Text = "지역 : " + sp.entity.address.ToList().FirstOrDefault(t => t.ano.Equals(h.ano)).aName;
            textBox1.Text = h.explanation;

            var strs = h.rno.Split(',');
            flowLayoutPanel1.Padding = new Padding(10, 10, 10, 10);

            dataGridView1.DataSource = sp.entity.roomtype.ToList()
                .Where(t => strs.Contains(t.rtno.ToString()))
                .Select(t => new { 객실타입 = t.rtname, 박요금 = t.baseprice * h.rating.percentage }).ToList();
            dataGridView1.Columns[1].HeaderText = "1박요금";

            flowLayoutPanel1.Controls.Add(barStar());
            sp.entity.review.ToList().Where(t => t.reservation.hno.Equals(h.hno)).ToList()
                .ForEach(t => {
                    Label label = new Label() {
                        Text = new string('★', t.score == null ? 0 : t.score.Value),
                        ForeColor = Color.Yellow,
                        Size = new Size(flowLayoutPanel1.Width - SystemInformation.VerticalScrollBarWidth - 20, Font.Height),
                    };
                    flowLayoutPanel1.Controls.Add(label);
                    flowLayoutPanel1.Controls.Add(new Label() {
                        Text = t.review1,
                        BorderStyle = BorderStyle.FixedSingle,
                        TextAlign = ContentAlignment.MiddleLeft,
                        Size = new Size(flowLayoutPanel1.Width - SystemInformation.VerticalScrollBarWidth - 20, 30),
                        Margin = new Padding(5, 0, 5, 10),
                    });
                });
        }

        private Label barStar() {
            var valueDouble = sp.entity.review.ToList().Where(t => t.reservation.hno.Equals(h.hno)).Select(t => t.score).Average();
            double ag = Math.Round(valueDouble.Value * 10) / 10;
            string s1 = "평점 : " + ag;
            string s2 = ag >= 4 ? "최우수" : ag >= 3.5 ? "우수" : ag >= 3 ? "보통" : "미흡";
            Label l = new Label { Size = new Size(flowLayoutPanel1.Width - SystemInformation.VerticalScrollBarWidth - 20, 30) , AutoSize = false, BorderStyle = BorderStyle.FixedSingle};
            l.Paint += (s, e) => {
                Graphics g = e.Graphics;
                var r = new RectangleF(0, 0, l.Width * (((int)ag) * 0.2f), l.Height);
                g.FillRectangle(Brushes.RoyalBlue, r);
                StringFormat sf = new StringFormat() {
                    Alignment = StringAlignment.Near,
                    LineAlignment = StringAlignment.Center
                };
                g.DrawString(s1, sp.f(10), Brushes.Black, 5, 5);
                sf.Alignment = StringAlignment.Far;
                r.Width -= 15;
                g.DrawString(s2, sp.f(10), Brushes.Black, r, sf);

            };
            return l;
        }

        private void pictureBox2_Click(object sender, EventArgs e) {
            if(dataGridView1.CurrentRow == null) {
                sp.err("객실을 선택하세요");
                return;
            }
            Hide();
            new Label();
            Show();
        }
    }
}
