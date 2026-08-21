using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_3 {
    public partial class Login : UserControl {
        public Login() {
            InitializeComponent();
            button1.BackColor = sp.colors[sp.GetOut];
            textBox2.BackColor = Color.White;
            textBox1.KeyPress += (s, e) => {
                if(e.KeyChar != (char)Keys.Back && textBox1.TextLength == 13) { e.Handled = true; }
                if (!char.IsDigit(e.KeyChar) && e.KeyChar != (char) Keys.Back) { e.Handled = true; }
                if (e.KeyChar != (char)Keys.Back && (textBox1.TextLength == 3 || textBox1.TextLength == 8))
                    textBox1.Text += "-";
                textBox1.SelectionStart = textBox1.TextLength;
            };
        }

        private void setting() {
            textBox1.Text = "";
            textBox2.Text = "";
            tableLayoutPanel1.Controls.Clear();
            List<int> list = new List<int>();
            while (list.Count != 10) {
                int r = new Random().Next(10);
                if (!list.Contains(r)) list.Add(r);
            }
            list.ForEach(t => {
                Label l = new Label {
                    Text = t.ToString(),
                    TextAlign = ContentAlignment.MiddleCenter,
                    BackColor = Color.White,
                    BorderStyle = BorderStyle.FixedSingle,
                    Dock = DockStyle.Fill,
                };

                l.Click += (sender, e) => {
                    textBox2.Text += t.ToString();
                };
                tableLayoutPanel1.Controls.Add(l);
            });

            Label delete = new Label {
                ImageAlign = ContentAlignment.MiddleCenter,
                BackColor = Color.White,
                BorderStyle = BorderStyle.FixedSingle,
                Dock = DockStyle.Fill,
            };
            delete.Image = sp.changeImage(Properties.Resources.delete, new Size(delete.Height, delete.Height), Color.Black);

            Label deleteA = new Label {
                Text = "전체\n지움",
                TextAlign = ContentAlignment.MiddleCenter,
                BackColor = Color.White,
                BorderStyle = BorderStyle.FixedSingle,
                Dock = DockStyle.Fill,
            };

            delete.Click += (s, e) => {
                textBox2.Text = textBox2.Text.Substring(0, Math.Max(0, textBox2.TextLength - 1));
            };
            deleteA.Click += (s, e) => {
                textBox2.Text = "";
            };

            tableLayoutPanel1.Controls.Add(delete, 2, 0);
            tableLayoutPanel1.Controls.Add(deleteA, 0, 3);
        }

        private void Login_VisibleChanged(object sender, EventArgs e) {
            setting();
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;

            if (string.IsNullOrEmpty(s1) || string.IsNullOrEmpty(s2)) { sp.err("빈칸이 존재합니다. 다시 확인해주세요"); return; }
            if (s1.Equals("010-0000-0000") && s2.Equals("0000")) { sp.infor("관리자님 환영합니다."); return; }

            var test = sp.entity.user.ToList().Where(t => t.phone.Equals(s1));
            if(test.Count() == 0) {
                var a = new user();
                a.phone = textBox1.Text;
                a.pw = textBox2.Text;
                a.card = null;
                a.point = 0;
                sp.entity.user.Add(a);
                sp.entity.SaveChanges();

                sp.user = a;
                if (sp.check("카드를 등록하시겠습니까?") ==DialogResult.Yes) {
                    sp.Show("카드등록");
                } else { sp.Show("메인2"); }
            } else {
                sp.user = test.FirstOrDefault(t => t.pw.Equals(s2));
                if(sp.user == null) {
                    sp.err("회원 정보를 확인하시오");
                    return;
                }
                sp.infor(sp.user.phone + "님 환영합니다.");
                sp.Show("메인2");
            }
        }
    }
}
