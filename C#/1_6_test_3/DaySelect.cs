using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement.TaskbarClock;

namespace _1_6_test_3 {
    public partial class DaySelect : UserControl {
        Point p1, p2;

        private void radioButton1_CheckedChanged(object sender, EventArgs e) {
            panel1.Location = p1;
            panel2.Location = p2;
        }

        private void radioButton2_CheckedChanged(object sender, EventArgs e) {
            panel1.Location = p2;
            panel2.Location = p1;
        }

        private void DaySelect_Load(object sender, EventArgs e) {
            
        }

        private void label1_Click(object sender, EventArgs e) {
            if(time == -1) {
                sp.err("시간을 선택해주세요");
                return;
            }
            if (string.IsNullOrEmpty(textBox1.Text)) {
                sp.err("포인트를 입력해주세요.");
                return;
            }
            string card = textBox2.Text + textBox3.Text + textBox4.Text + textBox5.Text;
            if (!card.Equals(sp.user.card)) {
                sp.err("카드번호를 모두 입력해주세요");
                return;
            }
            sp.infor("좌석배치도폼으로 이동하겠습니다.");
        }
        int time = -1;
        public DaySelect() {
            InitializeComponent();
            p1 = panel1.Location; p2 = panel2.Location;
            radioButton1.Select();

            int[] ints = new int[] { 1, 3, 5, 8, 12 };
            foreach (var i in ints) {
                RoundButton b = new RoundButton(null, i + "시간", sp.colors[sp.ReservationInfor]) { Name = i + "b"};
                b.Click += (s, e) => {
                    foreach (var j in ints) { tableLayoutPanel1.Controls[j + "b"].BackColor = sp.colors[sp.ReservationInfor]; }
                    time = i;
                    b.BackColor = Color.Gray;
                };
                tableLayoutPanel1.Controls.Add(b);
            }

            for(int i = 2; i < 2 + 4; i++) {
                TextBox t = (TextBox)panel2.Controls["textBox" + i];
                TextBox t2 = (TextBox)panel2.Controls["textBox" + Math.Min(5, i + 1)];
                t.TextChanged += (s, e) => {
                    if (t.TextLength >= 4) {
                        t.Text = t.Text.Substring(0, 4);
                        t2.Focus();
                    }
                };
            }
        }
    }
}
