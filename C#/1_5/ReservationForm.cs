using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.Entity.Infrastructure;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {
    public partial class ReservationForm : Form {
        hotel hotel;
        double? avg;
        string gradeTest;

        List<DateTime> dates = new List<DateTime>();

        public ReservationForm(int hno) {
            hotel = sp.entity.hotel.ToList().Find(t => t.hno == hno);
            avg = sp.entity.review.ToList().Where(t => t.reservation.hno == hno).Select(t => t.score).Average();
            gradeTest = avg > 4 ? "최우수" : avg > 3.5 ? "우수" : avg > 3 ? "보통" : "미흡";

            InitializeComponent();
            hotelImage.Image = (Image)Properties.Resources.ResourceManager.GetObject("_" + hotel.hno);
            hotelName.Text = hotel.hName;

            gradeLabel.Text = "등급 : " + hotel.ratno + "성급(" + new string('★', hotel.ratno.Value) + ")";
            citylabel.Text = "지역 : " + sp.entity.address.ToList().Find(t => t.ano == hotel.ano).aName;
            hotelInforLabel.Text = hotel.explanation;
            settingReview();
            settingDataGrid();
        }

        private void settingDataGrid() {
            var rnos = hotel.rno.Split(',');
            dataGridView1.DataSource = sp.entity.roomtype.ToList()
                .Where(t => rnos.Contains(t.rtno.ToString()))
                .Select(t => new { 번호 = t.rtno, 객실타입 = t.rtname, 요금 = (int) (t.baseprice * hotel.rating.percentage) }).ToList();
            dataGridView1.Columns[2].HeaderText = "1박요금";
            dataGridView1.Columns[0].Visible = false;
        }

        private void settingReview() {
            Label l = new Label() {
                Text = "평점 : " + avg.Value,
                BorderStyle = BorderStyle.FixedSingle,
                Size = new Size(flowLayoutPanel1.Width - SystemInformation.VerticalScrollBarWidth - 12, 30),
                Margin = new Padding(5, 10, 5, 10),

            };
            l.Paint += starPaint;
            flowLayoutPanel1.Controls.Add(l);
            foreach (var re in sp.entity.review.ToList().Where(t => t.reservation.hno == hotel.hno)) {
                Label label = new Label() {
                    Text = new string('★', re.score.Value),
                    ForeColor = Color.Yellow,
                    Size = new Size(flowLayoutPanel1.Width - SystemInformation.VerticalScrollBarWidth - 12, Font.Height),
                };
                flowLayoutPanel1.Controls.Add(label);
                flowLayoutPanel1.Controls.Add(new Label() {
                    Text = re.review1,
                    BorderStyle = BorderStyle.FixedSingle,
                    TextAlign = ContentAlignment.MiddleLeft,
                    Size = new Size(flowLayoutPanel1.Width - SystemInformation.VerticalScrollBarWidth - 12, 30),
                    Margin = new Padding(5, 0, 5, 10),
                });
            }
        }
        private void starPaint(object s, PaintEventArgs e) {
            Label l = s as Label;
            Graphics g = e.Graphics;
            var test = (int)avg * 0.2f;

            StringFormat sf = new StringFormat {
                Alignment = StringAlignment.Near,
                LineAlignment = StringAlignment.Center
            };

            g.FillRectangle(Brushes.RoyalBlue, 0, 0, l.Width * test, l.Height);
            g.DrawString("평점 : " + Math.Round(avg.Value * 10) / 10, sp.f(10)
                , Brushes.Black
                , new RectangleF(5, 0, l.Width / 2, l.Height), sf);
            sf.Alignment = StringAlignment.Far;
            g.DrawString(gradeTest, sp.f(10)
                , Brushes.Black
                , new RectangleF(5, 0, l.Width * test - 15, l.Height), sf);
        }

        private void pictureBox1_Click(object sender, EventArgs e) {
            if (dataGridView1.CurrentRow == null) { sp.err("객실을 선택하세요."); return; }
            Hide();
            new Moment(hotel.hno, (int)dataGridView1.CurrentRow.Cells[0].Value, dates).ShowDialog();
            Show();

            dateLabel.Text = dates.First().ToString("yyyy-MM-dd");
            dayLabel.Text = dates.Count.ToString() + "박";
            priceLabel.Text = ((int)(dataGridView1.CurrentRow.Cells[2].Value) * dates.Count).ToString("N0");
        }

        private void button1_Click(object sender, EventArgs e) {
            if (dates.Count == 0) {
                sp.err("객실 및 예약 날짜를 선택하세요.");
                return;
            }
            reservation r = new reservation();
            r.uno = sp.user.uno;
            r.hno = hotel.hno;
            r.sdate = dates.Min();
            r.rtno = (int)(dataGridView1.CurrentRow.Cells[0].Value);
            r.day = dates.Count;

            sp.entity.reservation.Add(r);
            sp.entity.SaveChanges();
            sp.infor("예약이 완료되었습니다.");
            Close();
        }
    }
}
