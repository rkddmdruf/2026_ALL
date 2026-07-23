using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1 {
    internal class getter {
        public static Font font = new Font("맑은 고딕", 9, FontStyle.Bold);
        public static AppUser user { get; set; }
        public static bool textIsBlanck(params TextBox[] ts) {
            foreach (TextBox t in ts) {
                if (t.Text.Length == 0) { err(t.Name + "비어 있습니다."); return false; }
            }
            return true;
        }

        public static void err(string s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        public static void inf(string s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
    }
}
