using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_3 {
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();
            label2.Text = sp.entity.Festival.ToList()[0].Name + " 운영 시스템";
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;

            if (string.IsNullOrEmpty(s1)) {
                sp.err("아이디 필수");
                return;
            }
            if (string.IsNullOrEmpty(s2)) {
                sp.err("비밀 번호 필수");
                return;
            }

            sp.user = sp.entity.AppUser.ToList().FirstOrDefault(t => t.Password.Equals(s2) && t.LoginId.Equals(s1));
            if(sp.user == null) {
                sp.err("아이디 또는 비밀번호가 올바르지 않습니다.");
                return;
            }

            Hide();
            new Main().ShowDialog();
            Show();
        }
    }
}
