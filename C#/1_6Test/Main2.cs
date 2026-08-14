using _1_6;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6Test {
    public partial class Main2 : UserControl {
        public Main2() {
            InitializeComponent();
            BackColor = Color.Transparent;

            int paddings = 10;
            Button b1 = new RoundButton(Properties.Resources.login, "입실", sp.colors[sp.Login]) { Margin = new Padding(paddings) };
            Button b2 = new RoundButton(Properties.Resources.cal, "예약", sp.colors[sp.Reservation]) { Margin = new Padding(paddings) };
            Button b3 = new RoundButton(Properties.Resources.walk, "외출/재입장", sp.colors[sp.Out]) { Margin = new Padding(paddings) };
            Button b4 = new RoundButton(Properties.Resources.power, "퇴실", sp.colors[sp.GetOut]) { Margin = new Padding(paddings) };
            b1.Click += (s, e) => {
                sp.Show("기간선택");
                sp.action.Push("메인2");
            };
            b4.Click += (s, e) => {
                sp.infor("퇴실되었습니다.");
                sp.user = null;
                sp.Show("메인1");
            };
            tableLayoutPanel1.Controls.Add(b1); tableLayoutPanel1.Controls.Add(b2); tableLayoutPanel1.Controls.Add(b3); tableLayoutPanel1.Controls.Add(b4);
        }

        private void Main2_VisibleChanged(object sender, EventArgs e) {
            if (sp.user != null) label2.Text = sp.grade();
        }

        private void pictureBox1_Click(object sender, EventArgs e) {
            sp.Show("카드번호등록/수정");
        }
    }
}
