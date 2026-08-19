using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_2 {
    public partial class DaySelect : UserControl {
        Point l1, l2;

        private void radioButton1_CheckedChanged(object sender, EventArgs e) {
            panel1.Location = l1; panel2.Location = l2;
        }

        private void radioButton2_CheckedChanged(object sender, EventArgs e) {
            panel1.Location = l2; panel2.Location = l1;
        }

        public DaySelect() {
            InitializeComponent();
            l1 = panel1.Location; l2 = panel2.Location;
            radioButton1.Select();
            point.Text = "포인트 보유량: " + sp.user.point.ToString("N0") + "pt";
            price.Text = "가격: " + "0" + "원";
            foreach (var s in new int[] {1, 3, 5, 8, 12})
            {
                RoundButton r = new RoundButton(null, s + "시간", sp.colors[sp.ReservationInfor]) { Dock = DockStyle.Fill, Padding = new Padding(0, 0, 0, 10), };
                tableLayoutPanel1.Controls.Add(r);
            }
        }
    }
}
