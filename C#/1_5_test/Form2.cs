using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test {
    public partial class _1_5_test_base1 : Form {
        public _1_5_test_base1() {
            InitializeComponent();
            radioButton1.Select();
            button1.BackColor = Color.White;
        }

        private void button1_Click(object sender, EventArgs e) {
            string s1 = textBox1.Text;
            string s2 = textBox2.Text;

            if (String.IsNullOrEmpty(s1) || String.IsNullOrEmpty(s2)) {
                sp.err("빈칸이 있습니다.");
                return;
            }

            sp.user = sp.entity.user.FirstOrDefault(x => x.id == s1 && x.pw == s2);
            sp.owner = sp.entity.owner.FirstOrDefault(x => x.id == s1 && x.pw == s2);

            if (sp.user == null && radioButton1.Checked) {
                sp.err("존재하는 회원이 없습니다.");
                textBox1.Text = "";
                textBox2.Text = "";
                return;
            }
            if (sp.owner == null && radioButton2.Checked) {
                sp.err("존재하는 회원이 없습니다.");
                textBox1.Text = "";
                textBox2.Text = "";
                return;
            }

            sp.infor((sp.user == null ? sp.owner.oname : sp.user.uname) + "님 환영합니다.");
            Close();
        }

        private void pictureBox1_Click(object sender, EventArgs e) {

        }

        private void label1_Click(object sender, EventArgs e) {
            
        }
    }
}
