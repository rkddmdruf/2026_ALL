using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1 {
    internal class sp {
        public static Entity entity = new Entity();
        public static AppUser user;
        public static Font f(int size, FontStyle s = FontStyle.Regular) {
            return new Font("맑은 고딕", size, s);
        }

        public static Font fk(int size, FontStyle s = FontStyle.Bold) {
            return new Font("맑은 고딕", size, s);
        }

        public static void err(string s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        public static void infor(string s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        public static Color setA(Color c, int a) {
            return Color.FromArgb(a, c.R, c.G, c.B);
        }
    }
}
