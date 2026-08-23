using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_5 {
    public partial class Main1 : UserControl {
        public Main1() {
            InitializeComponent();
            RoundButton b1 = new RoundButton(Properties.Resources.login, "로그인", sp.colors[sp.login]);
            RoundButton b2 = new RoundButton(Properties.Resources.seatlist, "예약현황", sp.colors[sp.ReservationInfor]);

            tableLayoutPanel1.Controls.Add(b1, 0, 0);
            tableLayoutPanel1.Controls.Add(b2, 2, 0);

            b1.Click += (s, e) => {
                sp.Show("로그인");
            };
        }
    }
}
