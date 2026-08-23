using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_5 {
    public partial class DaySelect : UserControl {
        Point p1, p2;
        List<int> ints = new List<int> { 1, 3, 5, 8, 12 };
        List<RoundButton> buttons = new List<RoundButton>();
        public DaySelect() {
            InitializeComponent();
            p1 = panel1.Location; p2 = panel2.Location;
            for (int i = 2; i <= 5; i++) {
                var t = (TextBox)panel2.Controls["textBox" + i];
                var t2 = (TextBox)panel2.Controls["textBox" + Math.Min(5, i + 1)];
                t.KeyPress += (s, e) => {
                    if (!char.IsDigit(e.KeyChar) && (char) Keys.Back != e.KeyChar) {
                        e.Handled = true;
                    }
                    if (t.TextLength >= 4 && (char)Keys.Back != e.KeyChar) {
                        t2.Focus();
                        e.Handled = true;
                    }
                };
            }
            foreach (var i in ints) {
                RoundButton r = new RoundButton(null, i + "시간", sp.colors[sp.ReservationInfor]);
                buttons.Add(r);
                r.Click += (s, e) => {
                    buttons.ForEach(t => t.BackColor = sp.colors[sp.ReservationInfor]);
                    r.BackColor = Color.Gray;
                };
                tableLayoutPanel1.Controls.Add(r);
            }
        }

        private void radioButton1_CheckedChanged(object sender, EventArgs e) {
            panel1.Location = p1; panel2.Location = p2;
        }

        private void radioButton2_CheckedChanged(object sender, EventArgs e) {
            panel1.Location = p2; panel2.Location = p1;
        }

        private void DaySelect_VisibleChanged(object sender, EventArgs e) {
            textBox1.Text = "";
            timeLabel.Text = sp.selectTime == null || sp.selectTime.Date.Equals(new DateTime(1119, 11, 19)) ? "" : ("선택날짜: " + sp.selectTime.ToString("yyyy-MM-dd(dddd) HH:mm"));
            for (int i = 2; i <= 5; i++) panel2.Controls["textBox" + i].Text = "";
            pointLabel.Text = "포인트 보유량 " + sp.user.point.ToString("N0") + "pt";
        }

        private void label1_Click(object sender, EventArgs e) {
            sp.Show("달력");
        }

        private void DaySelect_Load(object sender, EventArgs e) {
            radioButton1.Select();
        }
    }
}
