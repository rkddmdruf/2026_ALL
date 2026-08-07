using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Net.Mime.MediaTypeNames;

namespace _1_6 {
    public partial class CardUpdate : UserControl {
        List<TextBox> list = new List<TextBox>();
        public string[] str = { "","","","" };
        RoundButton rb;
        public CardUpdate() {
            InitializeComponent();
            list.Add(textBox1); list.Add(textBox2); list.Add(textBox3); list.Add(textBox4);
            rb = new RoundButton(null, "등록", sp.colors[sp.Reservation]) {
                Dock = DockStyle.None,
                Location = button1.Location,
                Size = button1.Size,
                Font = sp.f(10)
            };
            backPanel.Controls.Add(rb);

            backPanel.Controls.Remove(button1);

            rb.Click += (s, e) => {
                str.ToList().ForEach(t => {
                    if (t.Length != 4) {
                        sp.err("칸 맞춰");
                        return;
                    }
                });
                sp.user.card = String.Join("", list.Select(l => l.Text).ToArray());
                sp.entity.SaveChanges();
                sp.Show("메인2");
            };

            list.ForEach(t => {
                t.Font = sp.fk(12);
                t.ForeColor = Color.White;
                t.KeyDown += (s, e) => { if(e.KeyCode == Keys.Enter) t.Text = str[list.IndexOf(t)].ToString(); t.SelectionStart = t.TextLength; };
                t.KeyPress += (s, e) => {
                    if (!char.IsDigit(e.KeyChar) && e.KeyChar != (char)Keys.Back) {
                        e.Handled = true;
                        return;
                    }

                    if (str[list.IndexOf(t)].Length >= 4 && e.KeyChar != (char) Keys.Back) {
                        MessageBox.Show(str[list.IndexOf(t)]);
                        e.Handled = true;
                        return;
                    }
                    if (e.KeyChar == (char)Keys.Back && str[list.IndexOf(t)].Length != 0)
                        str[list.IndexOf(t)] = str[list.IndexOf(t)].Substring(0, str[list.IndexOf(t)].Length - 1);
                    else if(e.KeyChar != (char)Keys.Back) str[list.IndexOf(t)] += e.KeyChar.ToString();
                    e.Handled = true;
                };
                t.Leave += (s, e) => {
                    t.Text = str[list.IndexOf(t)].ToString();
                };
            });

            this.VisibleChanged += (s, e) => {
                if (Visible) {
                    chageCardColor();
                    setButton();
                }
            };
        }

        private void setButton() {

        }
        private void chageCardColor() {
            Random r = new Random();
            Color color = Color.FromArgb(r.Next(256), r.Next(256), r.Next(256));
            list.ForEach(t => t.BackColor = color);

            if (sp.user.card != null) {
                rb.Text = "수정";
                string text = sp.user.card;
                List<string> parts = new List<string>();

                for (int i = 0; i < text.Length; i += 4) {
                    int len = Math.Min(4, text.Length - i);
                    parts.Add(text.Substring(i, len));
                }
                parts.ForEach(p => { list[parts.IndexOf(p)].Text = p.ToString(); str[parts.IndexOf(p)] = p.ToString(); });
                list.ForEach(t => t.Parent.BackColor = Color.Transparent);
            } else rb.Text = "등록";
            rb.Refresh();
            panel1.BackgroundImage = sp.changeImageColor(Properties.Resources.card, new Size(panel1.Width, panel1.Height), color);
        }
    }
}
