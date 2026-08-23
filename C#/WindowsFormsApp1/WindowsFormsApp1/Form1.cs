using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1 {
    public partial class Form1 : Form {
        List<UserControl> list = new List<UserControl>();
        Entity entity = new Entity();
        public Form1() {
            InitializeComponent();
            Text = "Festival Manager -- 로그인";
            ShowIcon = false;

            userText1.lb.Text = "아이디";
            userText1.tb.Name = "아이디";

            userText2.lb.Text = "비밀번호";
            userText2.tb.Name = "비밀번호";
            userText2.tb.PasswordChar = '●';

        }

        public static void Main(String[] args) {
            Application.Run(new Form1());
        }

        private void button1_Click(object sender, EventArgs e) {
            if (!getter.textIsBlanck(userText1.tb, userText2.tb)) return;
            var user = entity.AppUser.FirstOrDefault(a => a.LoginId.Equals(userText1.tb.Text) && a.Password.Equals(userText2.tb.Text));
            if (user == null) {
                getter.err("아이디 또는 비밀번호가 올바르지 않습니다.");
                return;
            }
            getter.user = user;
            Hide();
            userText1.tb.Text = "";
            userText2.tb.Text = "";
            new MainF().ShowDialog();
            Show();
        }
    }
}
