using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_5 {
    internal class sp {

        public static Entity entity = new Entity();
        public static AppUser user;
        public static Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();

        public static Font f(int size, FontStyle s = FontStyle.Regular) {
            return new Font("맑은 고딕", size, s);
        }

        public static Font fk(int size, FontStyle s = FontStyle.Bold) {
            return new Font("맑은 고딕", size, s);
        }

        public static void err(string s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        public static void Show(string s) {
            panels.Values.ToList().ForEach(t => t.Visible = false);
            if (panels.TryGetValue(s, out var p)) p.Visible = true;
        }

        public static void infor(string s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
        public static DialogResult check(string s) {
            return MessageBox.Show(s, "확인", MessageBoxButtons.YesNo, MessageBoxIcon.Question);
        }

        public static Color setA(Color c, int n = 60) {
            return Color.FromArgb(n, c.R, c.G, c.B);
        }
    }
}
