using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_3 {
    public partial class Login : Form {
        public Login() {
            InitializeComponent();
            radioButton1.Select();
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;

            if (string.IsNullOrEmpty(s1) || string.IsNullOrEmpty(s2)) {
                sp.err("빈칸이 있습니다.");
                return;
            }
            if((radioButton1.Checked && (sp.user = sp.entity.user.ToList().FirstOrDefault(t => t.id.Equals(s1) && t.pw.Equals(s2))) == null) ||
               (radioButton2.Checked && (sp.owner = sp.entity.owner.ToList().FirstOrDefault(t => t.id.Equals(s1) && t.pw.Equals(s2))) == null)) {
                textBox1.Text = "";
                textBox2.Text = "";

                textBox1.Select();
                sp.err("존재하는 회원이 없습니다.");
                return;
            }

            sp.infor(sp.user != null ? sp.user.uname + "회원님 환영합니다." : sp.owner.oname + "사장님 환영합니다.");
            Close();
        }
    }
}
