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
    public partial class Login : UserControl {
        Random r = new Random();
        public Login() {
            InitializeComponent();
            button1.BackColor = sp.colors[sp.GetOut];
            textBox2.BackColor = Color.White;
            reSetting();

            textBox1.KeyPress += (s, e) => {
                if (!char.IsDigit(e.KeyChar) && (int)Keys.Back != e.KeyChar) e.Handled = true;
                if ((textBox1.TextLength == 3 || textBox1.TextLength == 8) && (int)Keys.Back != e.KeyChar) { textBox1.Text += "-"; }
                if (textBox1.TextLength >= 13 && (int)Keys.Back != e.KeyChar) e.Handled = true;
                textBox1.SelectionStart = textBox1.TextLength;
                
            };
        }

        private void reSetting() {
            List<int> ints = new List<int>();
            tableLayoutPanel1.Controls.Clear();
            textBox1.Text = "";
            textBox2.Text = "";
            while(ints.Count != 10) {
                int n = r.Next(10);
                if(!ints.Contains(n)) { ints.Add(n); }
            }

            ints.ForEach(i => {
                Label l = new Label {
                    Text = i.ToString(),
                    Dock = DockStyle.Fill,
                    ForeColor = Color.Black,
                    BorderStyle = BorderStyle.FixedSingle,
                    TextAlign = ContentAlignment.MiddleCenter,
                    BackColor = Color.White,
                };
                l.Click += (s, e) => {
                    textBox2.Text += i.ToString();
                };
                tableLayoutPanel1.Controls.Add(l);
            });

            Label dl = new Label {
                Dock = DockStyle.Fill,
                BorderStyle = BorderStyle.FixedSingle,
                ImageAlign = ContentAlignment.MiddleCenter,
                BackColor = Color.White,
            };
            dl.Image = sp.changeImage(Properties.Resources.delete, new Size(dl.Height, dl.Height), Color.Black);

            Label adl = new Label {
                Text = "전체\n지움",
                Dock = DockStyle.Fill,
                ForeColor = Color.Black,
                BorderStyle = BorderStyle.FixedSingle,
                TextAlign = ContentAlignment.MiddleCenter,
                BackColor = Color.White,
            };
            dl.Click += (s, e) => {
                textBox2.Text = textBox2.Text.Substring(0, Math.Max(0, textBox2.TextLength - 1));
            };
            adl.Click += (s, e) => {
                textBox2.Text = "";
            };
            tableLayoutPanel1.Controls.Add(dl, 2, 0);
            tableLayoutPanel1.Controls.Add(adl, 0, 3);
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;

            if(string.IsNullOrWhiteSpace(s1)  || string.IsNullOrWhiteSpace(s2)) {
                sp.err("빈칸이 존재합니다. 확인해주세요.");
                return;
            }

            if(s1.Equals("010-0000-0000") && s2.Equals("0000")) {
                sp.err("관리자님 환영합니다.");
                return;
            }
            sp.user = sp.entity.user.ToList().FirstOrDefault(t => t.phone.Equals(s1));
            if(sp.user == null) {
                user u = new user();
                u.phone = s1;
                u.pw = s2;
                u.card = null;
                u.point = 0;
                sp.user = u;
                sp.entity.user.Add(u);
                sp.entity.SaveChanges();
                if(sp.check("카드를 등록하시겠습니까?", "확인") == DialogResult.Yes) {
                    sp.Show("카드번호수정/등록");
                    return;
                } else {
                    sp.Show("메인2");
                }

            } else if (!sp.user.pw.Equals(s2)) {
                sp.user = null;
                sp.err("회원 정보를 확인하시오.");
                return;
            }
            sp.infor(sp.user.phone + "님 환영힙낟.");
            sp.Show("메인2");
        }
    }
}
