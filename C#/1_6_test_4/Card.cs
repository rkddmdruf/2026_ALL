using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_4 {
    public partial class Card : UserControl {
        Random r = new Random();
        RoundButton b = new RoundButton(null, "등록", sp.colors[sp.ReservationInfor]);
        public Card() {
            InitializeComponent();
            reload();
            panel2.Controls.Add(b);
            b.Click += B_Click;
        }

        private void B_Click(object sender, EventArgs e) {
            string s = textBox1.Text + textBox2.Text + textBox3.Text + textBox4.Text;
            if (s.Length != 16) {
                sp.err("카드번호를 확인하시오");
                return;
            }else {
                sp.user.card = s;
                sp.entity.SaveChanges();
                sp.Show("메인2");
            }
        }

        private void reload() {
            Color color = Color.FromArgb(r.Next(0,256), r.Next(0, 256), r.Next(0, 256));
            imgPanel.BackgroundImage = sp.changeImage(Properties.Resources.card, new Size(imgPanel.Width, imgPanel.Height), color);
            b.Text = sp.user?.card == null ? "등록" : "수정";

            for(int i = 1; i <= 4; i++) {
                var t = (TextBox)imgPanel.Controls["textBox" + i];
                var t2 = (TextBox)imgPanel.Controls["textBox" + Math.Min(4, i + 1)];
                t.ForeColor = Color.White;
                t.BackColor = color;
                if(sp.user?.card != null) {
                    int n = (i - 1) * 4;
                    t.Text = sp.user.card.Substring(n, 4);
                };
                t.KeyPress += (s, e) => {
                    if (!char.IsDigit(e.KeyChar) && (char) Keys.Back != e.KeyChar) { e.Handled = true; }
                    if(t.TextLength >= 4 && (char)Keys.Back != e.KeyChar) {

                        t.Text = t.Text.Substring(0, 4);
                        t2.Select();
                        e.Handled = true;
                    }
                };
            }
        }

    }
}
