using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6 {
    internal class sp {
        internal static Entity entity = new Entity();
        internal static user user = entity.user.ToList()[0];
        internal static Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        internal static Form1 main;
        internal static Stack<string> action = new Stack<string>();

        internal static Font f(int size, FontStyle style = FontStyle.Regular) {
            return new Font("맑은 고딕", size, style);
        }
        internal static Font fk(int size, FontStyle style = FontStyle.Bold) {
            return new Font("맑은 고딕", size, style);
        }

        internal static int Login = 0;
        internal static int ReservationDetail = 1;
        internal static int Reservation = 2;
        internal static int Out = 3;
        internal static int GetOut = 4;

        internal static Color[] colors = new Color[] { 
            Color.FromArgb(68, 204, 174),
            Color.FromArgb(68,204,174),
            Color.FromArgb(50,128,50),
            Color.FromArgb(104,166,92),
            Color.FromArgb(188,210,114),
            Color.FromArgb(176,207,63)
        };

        public static bool isBlacked(Color c, int n = 25) {
            return c.R < n && c.G < n && c.B < n;
        }
        public static void Show(string text) {
            if(text.Contains("main")) main.leftLabel.Visible = false;
            else main.leftLabel.Visible = true;

            foreach (var item in panels.Values)
                item.Visible = false;
            panels[text].Visible = true;
        }
        internal static void infor(string s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        internal static void err(string s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        [System.Runtime.InteropServices.DllImport("user32.dll", CharSet = System.Runtime.InteropServices.CharSet.Auto)]
        internal static extern IntPtr SendMessage(IntPtr hWnd, int msg, int wp, string lp);
    }
}
