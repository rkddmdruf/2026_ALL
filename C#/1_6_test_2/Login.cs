using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_2 {
    public partial class Login : UserControl {
        public Login() {
            InitializeComponent();

            textBox1.KeyPress += (s, e) => {
                if (textBox1.TextLength == 13 && (char)Keys.Back != e.KeyChar) { e.Handled = true; }
                if (!char.IsDigit(e.KeyChar) && (char) Keys.Back != e.KeyChar) { e.Handled = true; }
                if (e.KeyChar != (char) Keys.Back)
                    if (textBox1.TextLength == 3 || textBox1.TextLength == 8) {
                        textBox1.Text += "-";
                    }
                textBox1.SelectionStart = textBox1.TextLength;
            };

            textBox2.BackColor = Color.White;
            button1.BackColor = sp.colors[sp.GetOut];
        }

        private void Login_VisibleChanged(object sender, EventArgs e) {
            textBox1.Text = "";
            textBox2.Text = "";
            tableLayoutPanel1.Controls.Clear();

            List<int> list = new List<int>();
            while(list.Count != 10) {
                int r = new Random().Next(10);
                if(!list.Contains(r)) { list.Add(r); }
            }

            list.ForEach(t => {
                Label l = new Label() {
                    Text = t.ToString(),
                    TextAlign = ContentAlignment.MiddleCenter,
                    BorderStyle = BorderStyle.FixedSingle,
                    BackColor = Color.White,
                    Dock = DockStyle.Fill
                };
                l.Click += (s, e2) => {
                    textBox2.Text += t.ToString();
                };
                tableLayoutPanel1.Controls.Add(l);
            });
            Label delete = new Label() {
                TextAlign = ContentAlignment.MiddleCenter,
                BorderStyle = BorderStyle.FixedSingle,
                BackColor = Color.White,
                Dock = DockStyle.Fill
            };
            delete.Image = new Bitmap(Properties.Resources.delete, new Size(delete.Height, delete.Height));
            tableLayoutPanel1.Controls.Add(delete, 2, 0);

            delete.Click += (s, ee) => {
                textBox2.Text = textBox2.Text.Substring(0, Math.Max(0, textBox2.TextLength - 1));
            };
            Label deleteAll = new Label() {
                Text = "전체\n지움",
                TextAlign = ContentAlignment.MiddleCenter,
                BorderStyle = BorderStyle.FixedSingle,
                BackColor = Color.White,
                Dock = DockStyle.Fill
            };
            deleteAll.Click += (s, ee) => {
                textBox2.Text = "";
            };
            tableLayoutPanel1.Controls.Add(deleteAll, 0, 3);
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;
            if (string.IsNullOrEmpty(s1) || string.IsNullOrEmpty(s2)) {
                sp.err();
                return;
            }
            if (s1.Equals("010-0000-0000")) {
                sp.infor("관리자님 환영합니다.");
                return;
            }
            var test = sp.entity.user.ToList().Where(t => t.phone.Equals(s1) && t.pw.Equals(s2));
            if(test.Count() != 0) {
                sp.user = sp.entity.user.ToList().FirstOrDefault(t => t.phone.Equals(s1) && t.pw.Equals(s2));
                if(sp.user == null) {
                    sp.err("회원 정보를 확인하시오.");
                    return;
                } else {
                    sp.infor(sp.user.uno + "님 환영합니다.");
                    sp.Show("메인2");
                }
            } else {
                user u = new user();
                u.phone = textBox1.Text;
                u.pw = textBox2.Text;
                u.point = 0;
                u.card = null;
                sp.entity.user.Add(u);  
                sp.entity.SaveChanges();
                if(MessageBox.Show("가드를 등록하시겠습니까?", "확인", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes) {
                    sp.Show("카드번호수정/등록");
                } else {
                    sp.Show("메인2");
                }
            }
            
        }
    }
}
