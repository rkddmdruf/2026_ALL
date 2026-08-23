using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_5 {
    public partial class Login : Form {
        public Login() {
            InitializeComponent();
            Icon = Properties.Resources.logo;
            titleImage.Image = Properties.Resources.logo.ToBitmap();
            radioButton1.Select();
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;
            if (string.IsNullOrWhiteSpace(s1) || string.IsNullOrWhiteSpace(s2)) {
                sp.err("빈칸이 있습니다.");
                return;
            }

            sp.user = sp.entity.user.ToList().FirstOrDefault(t => t.id.Equals(s1) && t.pw.Equals(s2));
            sp.owner = sp.entity.owner.ToList().FirstOrDefault(t => t.id.Equals(s1) && t.pw.Equals(s2));

            if (sp.user == null && radioButton1.Checked) {
                textBox1.Text = "";
                textBox2.Text = "";
                textBox1.Focus();
                sp.err("존재하는 회원이 없습니다.");
                sp.owner = null;
                return;
            }
            if (sp.owner == null && radioButton2.Checked) {
                textBox1.Text = "";
                textBox2.Text = "";
                textBox1.Focus();
                sp.err("존재하는 회원이 없습니다.");
                sp.user = null;
                return;
            }

            sp.infor((sp.user == null ? sp.owner.oname + "사장" : sp.user.uname + "회원") + "님 환영합니다.");
            Close();
        }
    }
}
