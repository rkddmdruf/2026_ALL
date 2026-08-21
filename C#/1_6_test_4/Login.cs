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
    public partial class Login : UserControl {
        public Login() {
            InitializeComponent();
            button1.BackColor = sp.colors[sp.GetOut];
            textBox2.BackColor = Color.White;
            textBox1.KeyPress += (s, e) => {
                if (!char.IsDigit(e.KeyChar) && (char)Keys.Back != e.KeyChar) e.Handled = true;
                if (textBox1.TextLength >= 13 && (char)Keys.Back != e.KeyChar) {
                    textBox1.Text = textBox1.Text.Substring(0, 13);
                    e.Handled = true;
                }
                if ((textBox1.TextLength == 3 || textBox1.TextLength == 8) && (char)Keys.Back != e.KeyChar) textBox1.Text += "-";
                textBox1.SelectionStart = textBox1.TextLength;
            };
        }

        private void Login_VisibleChanged(object sender, EventArgs e2) {
            tableLayoutPanel1.Controls.Clear();
            List<int> ints = new List<int>();
            while(ints.Count != 10) {
                var r = new Random().Next(10);
                if(!ints.Contains(r)) ints.Add(r);
            }
            foreach(int i in ints) {
                Label l = new Label {
                    Text = i.ToString(),
                    TextAlign = ContentAlignment.MiddleCenter,
                    BorderStyle = BorderStyle.FixedSingle,
                    Dock = DockStyle.Fill,
                    AutoSize = false,
                    BackColor = Color.White,
                    ForeColor = Color.Black
                };
                l.Click += (s, e) => {
                    textBox2.Text += l.Text;
                };
                tableLayoutPanel1.Controls.Add(l) ;
            }

            Label dl = new Label {
                ImageAlign = ContentAlignment.MiddleCenter,
                BorderStyle = BorderStyle.FixedSingle,
                Dock = DockStyle.Fill,
                AutoSize = false,
                BackColor = Color.White,
                ForeColor = Color.Black
            };
            dl.Image = new Bitmap(Properties.Resources.delete, new Size(dl.Height, dl.Height));
            Label adl = new Label {
                Text = "전체\n지움",
                TextAlign = ContentAlignment.MiddleCenter,
                BorderStyle = BorderStyle.FixedSingle,
                Dock = DockStyle.Fill,
                AutoSize = false,
                BackColor = Color.White,
                ForeColor = Color.Black
            };
            tableLayoutPanel1.Controls.Add(adl, 0, 3);
            tableLayoutPanel1.Controls.Add(dl, 2, 0) ;

            dl.Click += (s, e) => textBox2.Text = textBox2.Text.Substring(0, Math.Max(0, textBox2.TextLength - 1));
            adl.Click += (s, e) => textBox2.Text = "";
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;
            if (string.IsNullOrWhiteSpace(s1) || string.IsNullOrWhiteSpace(s2)) {
                sp.err("빈칸이 존재합니다. 다시 확인하시오.");
                return;
            }
            if(s1.Equals("010-0000-0000") && s2.Equals("0000")) {
                sp.Show("");
                sp.infor("관리자님 환영합니다.");
                return;
            }
            sp.user = sp.entity.user.ToList().FirstOrDefault(t => t.phone.Equals(s1));
            if(sp.user == null) {
                sp.user = new user();
                sp.user.phone = s1;
                sp.user.pw = s2;
                sp.user.card = null;
                sp.user.point = 0;
                sp.entity.user.Add(sp.user);
                sp.entity.SaveChanges();
                if(sp.check("카드를 등록하시겠습니까?", "확인") == DialogResult.Yes) {
                    sp.Show("카드번호등록/수정");
                }
            } else {
                sp.user = sp.user.pw.Equals(s2) ? sp.user : null;
                if(sp.user == null) {
                    sp.err("회원 정보를 확인해주세요.");
                    return;
                }
                sp.infor(sp.user.phone + "님 환영합니다.");
            }
            sp.Show("메인2");

        }
    }
}
