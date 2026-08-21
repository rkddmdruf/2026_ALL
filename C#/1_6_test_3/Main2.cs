using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_3 {
    public partial class Main2 : UserControl {
        public Main2() {
            InitializeComponent();
            RoundButton b1 = new RoundButton(Properties.Resources.login, "입실", sp.colors[sp.login]);
            RoundButton b2 = new RoundButton(Properties.Resources.cal, "예약", sp.colors[sp.Reservation]);
            RoundButton b3 = new RoundButton(Properties.Resources.walk, "외출/재입장", sp.colors[sp.Out]);
            RoundButton b4 = new RoundButton(Properties.Resources.power, "퇴실", sp.colors[sp.GetOut]);
            
            b1.Click += (s, e) => {
                sp.Show("기간선택");
            };

            b4.Click += (s, e) => {
                sp.infor("퇴실되었습니다.");
                sp.user = null;
                sp.Show("메인1");
            };
            tableLayoutPanel1.Controls.Add(b1);
            tableLayoutPanel1.Controls.Add(b2);
            tableLayoutPanel1.Controls.Add(b3);
            tableLayoutPanel1.Controls.Add(b4);
        }

        private void pictureBox1_Click(object sender, EventArgs e) {
            sp.Show("카드등록");
        }

        private void Main2_VisibleChanged(object sender, EventArgs e) {
            if(sp.user != null) {
                int p = sp.user.point;
                gradeLabel.Text = (p >= 200_000 ? "VIP" : p >= 100_000 ? "골드" : p >= 50_000 ? "실버" : "브론즈") + "등급";
            }
        }
    }
}
