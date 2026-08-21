using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_3 {
    internal class sp {

        public static Entity entity = new Entity();
        public static user user;
        public static owner owner;
        public static void setImage(Form f) {
            f.Icon = Properties.Resources.logo;
        }
        public static void err(String s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        public static void infor(String s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        public static Font f(int size, FontStyle s = FontStyle.Regular) {
            return new Font("맑은 고딕", size, s);
        }
        public static Font fk(int size, FontStyle s = FontStyle.Bold) {
            return new Font("맑은 고딕", size, s);
        }
    }
}
