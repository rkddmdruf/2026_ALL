using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_3 {
    public partial class Card : UserControl {
        Random random = new Random();
        RoundButton b;
        public Card() {
            InitializeComponent();
            b = new RoundButton(null, "등록", sp.colors[sp.ReservationInfor]);
            b.Click += (s, e) => {
                string str = textBox1.Text + textBox2.Text + textBox3.Text + textBox4.Text;
                if (str.Length != 16) {
                    sp.err("번호 확인");
                    return;
                }
                sp.user.card = str;
                sp.entity.SaveChanges();
                sp.Show("메인2");
            };
        }

        private void Card_VisibleChanged(object sender, EventArgs e) {
            panel2.Controls.Add(b);
            b.Text = sp.user == null ? "등록" : "수정";
            string card = "";
            if (sp.user != null) {
                card = sp.user.card;
            }
            Color c = Color.FromArgb(random.Next(0, 255), random.Next(0, 255), random.Next(0, 255));
            panel4.BackgroundImage = sp.changeImage(Properties.Resources.card, new Size(panel4.Width, panel4.Height), c);
            for(int i = 1; i <= 4; i++) {
                var t = ((TextBox)panel4.Controls["textBox" + i]);
                t.Text = "";
                var t2 = ((TextBox)panel4.Controls["textBox" + Math.Min(4, i + 1)]);
                if (sp.user != null) {
                    int test = (i - 1) * 4;
                    t.Text = card.Substring(test, Math.Min(4, card.Length - test));
                }
                t.BackColor = c;
                t.ForeColor = Color.White;
                t.TextAlign = HorizontalAlignment.Center;
                t.KeyPress += (s, ee) => {
                    if (t.TextLength >= 4 && ee.KeyChar != (int) Keys.Back) {
                        t2.Focus();
                        ee.Handled = true;
                    }
                };
                t.KeyDown += (s, ee) => {
                    if (ee.KeyCode == Keys.Enter) t2.Focus();
                };
            }
        }
    }
}
