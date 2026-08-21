using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_4 {
    public partial class Main2 : UserControl {
        public Main2() {
            InitializeComponent();
            RoundButton b1 = new RoundButton(Properties.Resources.login, "입실", sp.colors[sp.login]) {
                Margin = new Padding(0, 0, 10, 10),
            };
            RoundButton b2 = new RoundButton(Properties.Resources.login, "예약", sp.colors[sp.Reservation]) {
                Margin = new Padding(10, 0, 0, 10),
            };
            RoundButton b3 = new RoundButton(Properties.Resources.login, "외출/재입장", sp.colors[sp.Out]) {
                Margin = new Padding(0, 10, 10, 0),
            };
            RoundButton b4 = new RoundButton(Properties.Resources.login, "퇴실", sp.colors[sp.GetOut]) {
                Margin = new Padding(10, 10, 0, 0),
            };
            tableLayoutPanel1.Controls.Add(b1); tableLayoutPanel1.Controls.Add(b2); tableLayoutPanel1.Controls.Add(b3); tableLayoutPanel1.Controls.Add(b4);

            b1.Click += (s, e) => {
                sp.Show("기간선택");
            };
            b2.Click += (s, e) => {
                sp.Show("달력");
            };
            b4.Click += (s, e) => {
                sp.entity.outing.ToList().Where(t => t.uno.Equals(sp.user.uno)).ToList().ForEach(t => { sp.entity.outing.Remove(t); });
                Dispose();
            };
        }

        private void pictureBox1_Click(object sender, EventArgs e) {
            sp.Show("카드번호등록/수정");
        }

        private void Main2_VisibleChanged(object sender, EventArgs e) {
            if (sp.user != null) { label2.Text = (sp.user.point >= 200_000 ? "VIP" : sp.user.point >= 100_000 ? "골드" : sp.user.point >= 50_000 ? "실버" : "브론즈") + "등급"; }


        }
    }
}
