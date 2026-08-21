using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Security.Permissions;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_4 {
    public partial class DaySelect : UserControl {
        int[] times = { 1, 3, 5, 8, 12 };
        List<RoundButton> buttons = new List<RoundButton>();
        Point p1, p2;
        int selectHour = -1;
        private void DaySelect_Load(object sender, EventArgs e2) {
            p1 = panel1.Location; p2 = panel2.Location;
            radioButton1.Select();
            for (int i = 0; i < 4; i++) 
                panel2.Controls["textBox" + (i + 2)].GotFocus += (s, e) => {
                    if (sp.user.card == null) sp.Show("카드번호등록/수정");
                };
        }

        private void radioButton1_CheckedChanged(object sender, EventArgs e) {
            if(radioButton1.Checked) { panel1.Location = p1; panel2.Location = p2; }
            else { panel1.Location = p2; panel2.Location = p1; }
            setPoint();
        }

        private void setPoint() {
            pointLabel.Text = "포인트 보유량: " + (sp.user?.point ?? 0).ToString("N0") + "pt";
        }

        private void DaySelect_VisibleChanged(object sender, EventArgs e) {
            label3.Text = sp.selectTime == null || sp.selectTime.Equals(new DateTime(1119, 11, 19)) ? "" : "선택날짜: " + sp.selectTime.ToString("yyyy-MM-dd(dddd) HH:mm");
            if (sp.selectTime != null || !sp.selectTime.Equals(new DateTime(1119, 11, 19))) {
                int h = sp.selectTime.Hour;
                int n = 0;
                foreach (var i in times) {
                    if (h + i > 24) {
                        buttons[n].Enabled = false;
                        buttons[n].BackColor = Color.Gray;
                    }
                    n++;
                }
            }
        }

        public DaySelect() {
            InitializeComponent();
            BackColor = Color.Transparent;
           
            foreach (int i in times) {
                RoundButton b = new RoundButton(null, i + "시간", sp.colors[sp.ReservationInfor]);
                buttons.Add(b);
                b.Click += (s, e) => {
                    selectHour = i;
                    buttons.Where(t => t.Enabled).ToList().ForEach(t => t.BackColor = sp.colors[sp.ReservationInfor]);
                    b.BackColor = Color.Gray;
                };
                tableLayoutPanel1.Controls.Add(b);
            }

            for(int i = 0; i < 4; i++) {
                var t2 = panel2.Controls["textBox" + Math.Min(5, (i + 3))];
                panel2.Controls["textBox" + (i + 2)].KeyPress += (s, e) => {
                    var t = s as Control;
                    if(t.Text.Length >= 4 && (char)Keys.Back != e.KeyChar) {
                        t2.Select();
                        e.Handled = true;
                    }
                    if (!char.IsDigit(e.KeyChar) && (char)Keys.Back != e.KeyChar) { e.Handled = true; }

                };
            }

            textBox1.KeyUp+= (s, e) => {
                if (!char.IsDigit((char) e.KeyCode) && Keys.Back != e.KeyCode) { e.Handled = true; }
                if (!string.IsNullOrWhiteSpace(textBox1.Text) && int.Parse(textBox1.Text) >= sp.user.point) textBox1.Text = sp.user.point.ToString();
                textBox1.SelectionStart = textBox1.TextLength;
            };
        }
    }
}
