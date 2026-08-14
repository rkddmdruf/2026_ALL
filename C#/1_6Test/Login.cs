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
    public partial class Login : UserControl {
        List<Label> list = new List<Label>();
        List<int> ints = new List<int>();
        public Login() {
            InitializeComponent();
            textBox2.BackColor = Color.White;
            button1.BackColor = Color.YellowGreen;

            textBox1.KeyPress += (s, e) => {
                if(textBox1.TextLength >= 13 && (char)Keys.Back != e.KeyChar) e.Handled = true;
                if (!char.IsDigit(e.KeyChar) && (char) Keys.Back != e.KeyChar) { e.Handled = true; return; }
                if ((textBox1.TextLength == 3 || textBox1.TextLength == 8) && (char)Keys.Back != e.KeyChar) textBox1.Text = textBox1.Text + "-";
                textBox1.SelectionStart = textBox1.TextLength;
            };
            textBox1.KeyUp += (s, e) => {
                char c = (char) e.KeyCode;
                if (!char.IsDigit(c) && (char)Keys.Back != c) { e.Handled = true; return; }
                if ((textBox1.TextLength == 3 || textBox1.TextLength == 8) && Keys.Back != e.KeyCode) textBox1.Text = textBox1.Text + "-";
                textBox1.SelectionStart = textBox1.TextLength;
            };
        }

        private void Login_VisibleChanged(object sender, EventArgs e) {
            textBox1.Text = "";
            textBox2.Text = "";
            ints.Clear();
            tableLayoutPanel1.Controls.Clear();
            while (ints.Count < 10) {
                int r = new Random().Next(10);
                if (!ints.Contains(r)) { ints.Add(r); }
            }

            ints.ForEach(i => {
                Label l = new Label {
                    Text = i.ToString(),
                    BackColor = Color.White,
                    Dock = DockStyle.Fill,
                    BorderStyle = BorderStyle.FixedSingle,
                    TextAlign = ContentAlignment.MiddleCenter,
                };
                l.Click += (s, ev) => {
                    textBox2.Text += i.ToString();
                };
                tableLayoutPanel1.Controls.Add(l);
            });
            Label l1 = new Label {
                Text = "전체\n지움",
                BackColor = Color.White,
                Dock = DockStyle.Fill,
                BorderStyle = BorderStyle.FixedSingle,
                TextAlign = ContentAlignment.MiddleCenter,
            }; 
            Label l2 = new Label {
                BackColor = Color.White,
                Dock = DockStyle.Fill,
                BorderStyle = BorderStyle.FixedSingle,
                TextAlign = ContentAlignment.MiddleCenter,
            };
            l2.Image = new Bitmap(Properties.Resources.delete, new Size(l2.Height, l2.Height));

            l1.Click += (s, ev) => {
                textBox2.Text = "";
            };
            l2.Click += (s, ev) => {
                textBox2.Text = textBox2.Text.Substring(0, Math.Max(textBox2.TextLength - 1, 0));
            }; 
            tableLayoutPanel1.Controls.Add(l1, 0, 3);
            tableLayoutPanel1.Controls.Add(l2, 2, 0);
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;
            if (s1.Length == 0 || s2.Length == 0) { sp.err("빈칸이 존재합니다. 다시 확인해주세요."); return; }
            if (s1.Equals("010-0000-0000") && s2.Equals("1234")) {
                sp.err("관리자님 환영합니다.");
                return;
            }
            sp.user = sp.entity.user.ToList().Find(t => t.phone.Equals(s1) && t.pw.Equals(s2));
            if (sp.user == null) {
                sp.err("회원 정보를 확인하시오.");
                return;
            }
            sp.infor(sp.user.uno + "님 환영합니다.");
            sp.Show("메인2");
        }
    }
}
