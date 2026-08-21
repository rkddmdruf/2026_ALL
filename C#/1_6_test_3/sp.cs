using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Security.Policy;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_3 {
    internal class sp {
        public static Entity entity = new Entity();
        public static user user;
        public static List<string> action = new List<string>();
        public static Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        public static Form1 main;
        public static DateTime selectTime = new DateTime(1119, 11, 19);
        public static List<Color> colors = new List<Color> {

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
        private static bool isBlacked(Color c, int n = 25) {
            if (c.A == 0 && c.R == 0 && c.G == 0 && c.B == 0) return false;
            return c.B < n && c.B < n && c.B < n;
        }
        public static Image changeImage(Image img, Size s, Color c) {
            Bitmap b = new Bitmap(img, s);
            for(int y = 0; y < b.Height; y++) {
                for (int x = 0; x < b.Width; x++) {
                    if (isBlacked(b.GetPixel(x, y))) b.SetPixel(x, y, c);
                }
            }
            return b;
        }
        public static Font f(int size, FontStyle s = FontStyle.Regular) {
            return new Font("맑은 고딕", size, s);
        }
        public static Font fk(int size, FontStyle s = FontStyle.Bold) {
            return new Font("맑은 고딕", size, s);
        }
        
        public static void Show(string s, bool b = true) {
            var key = panels.FirstOrDefault(t => t.Value.Visible).Key;
            if(b) action.Add(key);

            foreach (var item in panels.Values)
                item.Visible = false;
            if (panels.TryGetValue(s, out var p)) {
                p.Visible = true;
            }
            main.leftLabel.Visible = !s.Contains("메인");

        }
        public static void err(string s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        public static void infor(string s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        public static DialogResult check(string s) {
            return  MessageBox.Show(s, "확인", MessageBoxButtons.YesNo, MessageBoxIcon.Question);
        }
    }
}
