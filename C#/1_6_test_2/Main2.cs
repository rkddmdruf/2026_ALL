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
    public partial class Main2 : UserControl {
        public Main2() {
            InitializeComponent();
            RoundButton b1 = new RoundButton(Properties.Resources.login, "입실", sp.colors[sp.login]) {
                Dock = DockStyle.Fill,
            };
            RoundButton b2 = new RoundButton(Properties.Resources.cal, "예약", sp.colors[sp.Reservation]) {
                Dock = DockStyle.Fill,
            };
            RoundButton b3 = new RoundButton(Properties.Resources.walk, "외출/재입장", sp.colors[sp.Out]) {
                Dock = DockStyle.Fill,
            };
            RoundButton b4 = new RoundButton(Properties.Resources.power, "퇴실", sp.colors[sp.GetOut]) {
                Dock = DockStyle.Fill,
            };

            b1.Click += (s, e) => {
                sp.Show("기간선택");
            };
            gradeLabel.Text = (sp.user.point >= 200000 ? "VIP" : sp.user.point >= 100000 ? "골드" : sp.user.point >= 50000 ? "실버" : "브론즈") + "등급";

            tableLayoutPanel1.Controls.Add(b1, 0, 0);
            tableLayoutPanel1.Controls.Add(b2, 2, 0);
            tableLayoutPanel1.Controls.Add(b3, 0, 2);
            tableLayoutPanel1.Controls.Add(b4, 2, 2);
        }

        private void pictureBox1_Click(object sender, EventArgs e) {

        }
    }
}
