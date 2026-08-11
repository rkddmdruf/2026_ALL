using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {
    internal class sp {
        public static Entity entity = new Entity();
        public static user user = entity.user.First();
        public static owner owner;

        public static Font f(float size, FontStyle style = FontStyle.Regular) {
            return new Font("맑은 고딕", size, style);
        }
        public static Font fk(float size, FontStyle style = FontStyle.Bold) {
            return new Font("맑은 고딕", size, style);
        }
        public static void err(String s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        public static void infor(String s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        public static void setting(topPanel top, Form form) {
            form.Icon = Properties.Resources.logo;
            
        }
    }
}
