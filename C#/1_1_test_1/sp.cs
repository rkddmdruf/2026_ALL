using System;
using System.Collections.Generic;
using System.Data.Entity.Core.Objects.DataClasses;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_1 {
    internal class sp {
        public static Entity entity = new Entity();
        public static AppUser user;
        public static void err(string message) {
            MessageBox.Show(message, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        public static void infor(string message) {
            MessageBox.Show(message, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        public static Font f(int size, FontStyle fs = FontStyle.Regular) {
            return new Font("맑은 고딕", size, fs);
        }
        public static Font fk(int size, FontStyle fs = FontStyle.Bold) {
            return new Font("맑은 고딕", size, fs);
        }

        public static DialogResult check(string s) {
            return MessageBox.Show(s, "확인", MessageBoxButtons.YesNo, MessageBoxIcon.Question);
        }

        public static Color setA(Color c, int a = 255) {
            return Color.FromArgb(a, c.R, c.G, c.B);
        }
    }
}
