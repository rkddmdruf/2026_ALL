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

            tableLayoutPanel1.Controls.Add(new RoundButton(Properties.Resources.login, "입실", sp.colors[sp.Login]) {
                Dock = DockStyle.Fill,
                Anchor = AnchorStyles.Left | AnchorStyles.Right | AnchorStyles.Top | AnchorStyles.Bottom,
            }, 0, 0);
            tableLayoutPanel1.Controls.Add(new RoundButton(Properties.Resources.login, "예약", sp.colors[sp.Reservation]) {
                Dock = DockStyle.Fill,
                Anchor = AnchorStyles.Left | AnchorStyles.Right | AnchorStyles.Top | AnchorStyles.Bottom,
            }, 2, 0);
            tableLayoutPanel1.Controls.Add(new RoundButton(Properties.Resources.login, "외출/재입장", sp.colors[sp.Out]) {
                Dock = DockStyle.Fill,
                Anchor = AnchorStyles.Left | AnchorStyles.Right | AnchorStyles.Top | AnchorStyles.Bottom,
            }, 0, 2);
            tableLayoutPanel1.Controls.Add(new RoundButton(Properties.Resources.login, "퇴실", sp.colors[sp.GetOut]) {
                Anchor = AnchorStyles.Left | AnchorStyles.Right | AnchorStyles.Top | AnchorStyles.Bottom,
            }, 2, 2);
        }
    }
}
