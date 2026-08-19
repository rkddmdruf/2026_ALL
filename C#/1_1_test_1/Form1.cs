using _1_1_test_1;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_1 {
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();
            label2.Text = sp.entity.Festival.ToList()[0].Name + " 운영 시스템";
            textBox2.UseSystemPasswordChar = true;
        }

        private void button1_Click(object sender, EventArgs e) {
            if (string.IsNullOrEmpty(textBox1.Text)) {
                sp.err("아이디는 필수 입니다.");
                return;
            }
            if (string.IsNullOrEmpty(textBox2.Text)) {
                sp.err("비밀번호는 필수 입니다.");
                return;
            }
            sp.user = sp.entity.AppUser.ToList().FirstOrDefault(t => t.LoginId.Equals(textBox1.Text) && t.Password.Equals(textBox2.Text));
            if(sp.user is null) {
                sp.err("아이디 또는 비밀번호가 올바르지 않습니다.");
                return;
            }
            Hide();
            new Main().ShowDialog();
            Show();
            Close();
        }
    }
}
