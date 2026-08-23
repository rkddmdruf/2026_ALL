using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1 {
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();
            label2.Text = sp.entity.Festival.ToList()[0].Name + " 운영 시스템";
            textBox2.UseSystemPasswordChar = true;
        }

        private void button1_Click(object sender, EventArgs e) {
            sp.user = sp.entity.AppUser.ToList().FirstOrDefault(t => t.LoginId.Equals(textBox1.Text) && t.Password.Equals(textBox2.Text));
            if(sp.user is null) {
                sp.err("아이디 또는 비밀번호가 올바르지 않습니다.");
                return;
            }
            Hide();
            new MainForm().ShowDialog();
            Show();
            Close();
        }
    }
}
