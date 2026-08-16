using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5test1 {
    internal class sp {
        public static Entity entity = new Entity();
        public static user user;
        public static owner owner;

        public static Font f(int size, FontStyle fs = FontStyle.Regular) {
            return new Font("맑은 고딕", size, fs);
        }
        public static Font fk(int size, FontStyle fs = FontStyle.Bold) {
            return new Font("맑은 고딕", size, fs);
        }

        public static void err(string s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        public static void infor(string s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
    }
}
