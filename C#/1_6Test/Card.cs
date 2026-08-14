using _1_6;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6Test {
    public partial class Card : UserControl {
        RoundButton b1;
        public Card() {
            InitializeComponent();
            b1 = new RoundButton(null, "수정", sp.colors[sp.ReservationDetail]);
            b1.TextAlign = ContentAlignment.MiddleCenter;
            panel2.Controls.Add(b1);
            for(int i = 1; i <= 4; i++) {
                var con = (TextBox) panel1.Controls["textBox" + i];
                con.Font = sp.fk(11);
                con.BorderStyle = BorderStyle.None;
                con.TextAlign = HorizontalAlignment.Center;
                con.KeyPress += (s, e) => {
                    if (!char.IsDigit(e.KeyChar) && (char) Keys.Back != e.KeyChar) { e.Handled = true; }
                };
                con.KeyDown += (s, e) => {
                    if (e.KeyCode == Keys.Enter || (con.TextLength >= 4 && Keys.Back != e.KeyCode)) {
                        SelectNextControl((Control)s, true, true, true, true);
                        e.SuppressKeyPress = true;   // 삑 소리 제거
                    }
                };
            }
            b1.Click += b1_Click;
        }

        private void Card_VisibleChanged(object sender, EventArgs e) {
            if(sp.user != null) {
                List<String> strs = new List<String>();
                for (int i = 0; i < 4; i++)
                    panel1.Controls["textBox" + (i + 1)].Text = sp.user.card.Substring(i * 4, 4);

            }

            Random r = new Random();
            Color c = Color.FromArgb(r.Next(255), r.Next(255), r.Next(255));
            panel1.BackgroundImage = sp.changeImageColor(Properties.Resources.card, panel1.Size, c);
            for (int i = 1; i <= 4; i++) {
                var con = (TextBox)panel1.Controls["textBox" + i];
                con.BackColor = c;
            }
        }

        private void b1_Click(object sender, EventArgs e) {
            sp.user.card = textBox1.Text + textBox2.Text + textBox3.Text + textBox4.Text;
            sp.entity.SaveChanges();
            sp.Show("메인2");
        }
    }
}
