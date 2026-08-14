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
    public partial class Main1 : UserControl {
        public Button rb1 = new RoundButton(Properties.Resources.login, "로그인", sp.colors[sp.Login]);
        public Button rb2 = new RoundButton(Properties.Resources.seatlist, "예약현황", sp.colors[sp.ReservationDetail]);
        public Main1() {
            InitializeComponent();
            BackColor = Color.Transparent;

            tp1.Controls.Add(rb1, 0, 0);
            tp1.Controls.Add(rb2, 2, 0);

            rb1.Click += (s, e) => {
                sp.Show("로그인");
            };
        }
    }
}
