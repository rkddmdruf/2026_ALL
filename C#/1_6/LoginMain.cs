using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6 {
    public partial class LoginMain : UserControl {
        public LoginMain() {
            InitializeComponent();
            /*
             button1.Image = new Bitmap(Properties.Resources.delete, new Size(30, 30));
            button1.Text = "텍스트";
            button1.Padding = new Padding(0, 30, 0, 0);
            button1.TextImageRelation = TextImageRelation.ImageAboveText;
            button1.ImageAlign = ContentAlignment.MiddleCenter;
            button1.TextAlign = ContentAlignment.MiddleCenter;*/

            Button b1 = new RoundButton(Properties.Resources.login, "입실", sp.colors[sp.Login]) {
                Dock = DockStyle.Fill,
            };
            Button b2 = new RoundButton(Properties.Resources.cal, "예약", sp.colors[sp.Reservation]) {
                Dock = DockStyle.Fill,
            };
            Button b3 = new RoundButton(Properties.Resources.walk, "외출/재입장", sp.colors[sp.Out]) {
                Dock = DockStyle.Fill,
            };
            Button b4 = new RoundButton(Properties.Resources.login, "퇴실", sp.colors[sp.GetOut]) {
                Dock = DockStyle.Fill,
            };

            tableLayoutPanel1.Controls.Add(b1, 0, 0);
            tableLayoutPanel1.Controls.Add(b2, 2, 0);
            tableLayoutPanel1.Controls.Add(b3, 0, 2);
            tableLayoutPanel1.Controls.Add(b4, 2, 2);

            label1.Text = sp.grade();
            b1.Click += (s, e) => {
                sp.Show("기간선택");
            };
            b2.Click += (s, e) => {
                sp.Show("달력");
            };
            pictureBox1.Click += (s, e) => {
                sp.Show("카드번호등록/수정");
            };
        }
    }
}
