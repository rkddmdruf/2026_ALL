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
    public partial class NoLoginMain : UserControl {

        [System.Runtime.InteropServices.DllImport("user32.dll", CharSet = System.Runtime.InteropServices.CharSet.Auto)]
        static extern IntPtr SendMessage(IntPtr hWnd, int msg, int wp, string lp);

        internal Button button1 = new RoundButton(Properties.Resources.login, "로그인", sp.colors[sp.Login]);
        internal Button button2 = new RoundButton(Properties.Resources.login, "예약현황", sp.colors[sp.ReservationDetail]);
        TextBox tb1 = new TextBox();
        public NoLoginMain() {
            InitializeComponent();
            BackColor = Color.Transparent;
            tableLayoutPanel1.Controls.Add(button1, 0, 0);
            tableLayoutPanel1.Controls.Add(button2, 2, 0);

            tb1.Dock = DockStyle.Fill;

            SendMessage(tb1.Handle, 0x1501, 0, "이름을 입력하세요");
            button1.Click += (s, e) => {
                sp.Show("login");
                sp.action.Push("nologinmain");
            };
        }
    }
}
