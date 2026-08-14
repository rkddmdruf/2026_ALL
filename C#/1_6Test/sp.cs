using _1_6Test;
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
        internal static user user;
        internal static Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        internal static Form1 main;
        internal static Stack<string> action = new Stack<string>();
        public static DateTime? momentFormToSelectDate;
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
            Color.FromArgb(50,128,50),
            Color.FromArgb(104,166,92),
            Color.FromArgb(188,210,114),
            Color.FromArgb(176,207,63)
        };

        public static string grade() {
            int p = user.point;
            return (p >= 200_000 ? "VIP" : p >= 100_000 ? "골드" : p >= 50_000 ? "실버" : "브론즈") + "등급";
        }
        public static bool isBlacked(Color c, int n = 25) {
            return c.R < n && c.G < n && c.B < n && c.A != 0;
        }

        public static Bitmap changeImageColor(Image img, Size size, Color? color = null) {
            color = color ?? Color.White;
            var bit = new Bitmap(img, size);
            for (int y = 0; y < bit.Height; y++) {
                for (int x = 0; x < bit.Width; x++) {
                    if (isBlacked(bit.GetPixel(x, y))) bit.SetPixel(x, y, color.Value);
                }
            }
            return bit;
        }
        public static void Show(string text) {
            main.Text = text.Contains("메인") ? "메인" : text;
            foreach (var item in panels.Values) item.Visible = false;
            if (panels.TryGetValue(text, out var p)) p.Visible = true;
            if (text.Contains("메인")) action.Clear();
        }
        internal static void infor(string s) {
            MessageBox.Show(s, "정보", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
        internal static void err(string s) {
            MessageBox.Show(s, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }
}
