using System.Drawing;
using System.Windows.Forms;

namespace _1_6_test_2 {
    public partial class Main1 : UserControl {
        public Main1() {
            InitializeComponent();

            RoundButton rb1 = new RoundButton(Properties.Resources.login, "로그인", sp.colors[sp.login]) {
                Dock = DockStyle.Fill
            };
            RoundButton rb2 = new RoundButton(Properties.Resources.seatlist, "예약현황", sp.colors[sp.ReservationInfor]) {
                Dock = DockStyle.Fill
            };

            tableLayoutPanel1.Controls.Add(rb1);
            tableLayoutPanel1.Controls.Add(new Label() { });
            tableLayoutPanel1.Controls.Add(rb2);

            rb1.Click += (s, e) => {
                sp.Show("로그인");
            };
        }
    }
}
