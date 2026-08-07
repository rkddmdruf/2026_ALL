using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {
    public partial class Login : Form {
        public Login() {
            InitializeComponent();
            radioButton1.Select();
            
            
        }

        private void button1_Click(object sender, EventArgs e) {
            string id = textBox1.Text;
            string pw = textBox2.Text;

            if(id.Length == 0 || pw.Length == 0) {
                sp.err("빈칸이 있습니다.");
                return;
            }

            if(radioButton1.Checked && (sp.user = sp.entity.user.FirstOrDefault(u => u.id.Equals(id) && u.pw.Equals(pw))) != null) {
                sp.infor(sp.user.uname + "회원님 환영합니다."); Close(); return;
            }
            if (radioButton2.Checked && (sp.owner = sp.entity.owner.FirstOrDefault(o => o.id.Equals(id) && o.pw.Equals(pw))) != null) {
                sp.infor(sp.owner.oname + "사장님 환영합니다."); Close(); return;
            }

            sp.err("존재하는 회원이 없습니다.");
            textBox1.Text = "";
            textBox2.Text = "";
            textBox1.Focus();
        }
    }
}
