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
    public partial class Card : UserControl {
        Random r = new Random();
        RoundButton b = new RoundButton(null, "수정", sp.colors[sp.ReservationInfor]);
        public Card() {
            InitializeComponent();
            cardPanel.BackgroundImageLayout = ImageLayout.Stretch;
            panel2.Controls.Add(b);
            for (int i = 1; i <= 4; i++) {
                var t = (TextBox)cardPanel.Controls["textBox" + i];
                var t2 = (TextBox)cardPanel.Controls["textBox" + Math.Min(4, i + 1)];
                t.KeyPress += (s, e) => {
                    if(!char.IsDigit(e.KeyChar) && (char) Keys.Back != e.KeyChar) { e.Handled = true; }
                    if(t.TextLength >= 4 && (char)Keys.Back != e.KeyChar) {
                        t2.Focus();
                        e.Handled = true;
                    }
                };
            }
            b.Click += B_Click;
        }

        private void B_Click(object sender, EventArgs e) {
            string card = textBox1.Text + textBox2.Text + textBox3.Text + textBox4.Text;
            if(card.Length != 16) {
                sp.err("번호 16자리");
                return;
            }
            sp.user.card = card;
            sp.entity.SaveChanges();
            sp.Show("메인2");
        }

        private void Card_VisibleChanged(object sender, EventArgs e) {
            Color c = Color.FromArgb(r.Next(256), r.Next(256), r.Next(256));

            b.Text = sp.user.card == null ? "등록" : "수정";
            for(int i = 1; i <= 4; i++) {
                int n = (i - 1) * 4;
                cardPanel.Controls["textBox" + i].BackColor = c;
                cardPanel.Controls["textBox" + i].Text = sp.user.card.Substring(n, Math.Min(4, sp.user.card.Length - n));
            }
            cardPanel.BackgroundImage = sp.changeImage(Properties.Resources.card, new Size(cardPanel.Width, cardPanel.Height), c);
        }
    }
}
