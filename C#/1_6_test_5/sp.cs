using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_5 {
    internal class sp {
        public static Entity entity = new Entity();
        public static user user;

        public static Form1 main;
        public static Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        public static List<string> action = new List<string>();
        public static Color[] colors = {
            Color.FromArgb(68,204,174),
            Color.FromArgb(50,128,50),
            Color.FromArgb(104,166,92),
            Color.FromArgb(188,210,114),
            Color.FromArgb(176,207,63)
        };
        public static int login = 0;
        public static int ReservationInfor = 1;
        public static int Reservation = 2;
        public static int Out = 3;
        public static int GetOut = 4;
        internal static DateTime selectTime = new DateTime(1119, 11, 19);

        public static Font f(float size, FontStyle s = FontStyle.Regular) {
            return new Font("맑은 고딕", size, s);
        }
        public static Font fk(float size, FontStyle s = FontStyle.Bold) {
            return new Font("맑은 고딕", size, s);
        }
        public static void err(string s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        public static void infor(string s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
        public static DialogResult check(string s, string s2) {
            return MessageBox.Show(s, s2, MessageBoxButtons.YesNo, MessageBoxIcon.Question);
        }

        static bool isBlacked(Color c, int n = 25) {
            if (c.A == 0 && c.R == 0 && c.G == 0 && c.B == 0) { return false; }
            return c.R < n && c.B < n && c.G < n;
        }
        public static Image changeImage(Image img, Size size, Color color) {
            var b = new Bitmap(img, size);
            for (int y = 0; y < b.Height; y++) {
                for (int x = 0; x < b.Width; x++) {
                    if(isBlacked(b.GetPixel(x, y))) b.SetPixel(x, y, color);
                }
            }
            return b;
        }

        public static void Show(string s, bool b = true) {
            main.Text = s.Contains("메인") ? "메인" : s;
            var key = panels.Keys.ToList().FirstOrDefault(t => panels[t].Visible);
            panels.Values.ToList().ForEach(t => t.Visible = false);
            if (panels.TryGetValue(s, out var panel)) {
                panel.Visible = true;
            }
            main.leftLabel.Visible = true;
            if (b) action.Add(key);
            if (s.Contains("메인")) {
                action.Clear();
                main.leftLabel.Visible = false;
            }
        }
    }
}
