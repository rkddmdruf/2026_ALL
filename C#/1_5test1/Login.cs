using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5test1 {
    public partial class Login : Form {
        public Login() {
            InitializeComponent();
            radioButton1.Select();
            button1.BackColor = Color.White;


        }

        private void button1_Click(object sender, EventArgs e) {
            string i = textBox1.Text;
            string p = textBox2.Text;

            if (string.IsNullOrEmpty(i) || string.IsNullOrEmpty(p)) {
                sp.err("빈칸이 있습니다.");
                return;
            }

            sp.user = sp.entity.user.ToList().Find(t => t.id.Equals(i) && t.pw.Equals(p));
            sp.owner = sp.entity.owner.ToList().Find(t => t.id.Equals(i) && t.pw.Equals(p));

            if (radioButton2.Checked && sp.owner == null) {
                sp.err("존재하는 회원이 없습니다.");
                textBox1.Text = "";
                textBox2.Text = "";
                textBox1.Select();
                return;
            }

            if (radioButton1.Checked && sp.user == null) {
                sp.err("존재하는 회원이 없습니다.");
                textBox1.Text = "";
                textBox2.Text = "";
                textBox1.Select();
                return;
            }

            sp.infor((sp.user is null ? sp.owner.oname + "사장" : sp.user.uname + "회원") + "님 환영합니다.");
            Close();
        }
    }
}
