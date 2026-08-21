using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_3 {
    internal class RoundButton : Button{

        public RoundButton(Image img, string str, Color color) {
            Dock = DockStyle.Fill;
            FlatStyle = FlatStyle.Flat;
            FlatAppearance.BorderSize = 0;
            Region = null;

            Font = sp.f(10);
            ForeColor = Color.White;
            BackColor = color;

            if (img != null) Image = sp.changeImage(img, new Size(60, 60), Color.White);
            Text = str;

            TextImageRelation = TextImageRelation.ImageAboveText;
            ImageAlign = ContentAlignment.TopCenter;
            if (img == null) TextAlign = ContentAlignment.MiddleCenter;
            else TextAlign = ContentAlignment.BottomCenter;

            Resize += (s, e) => {
                int hi = (int) (Font.Height * 1.4 + 10) + Image?.Height ?? 0;
                if (img == null) Padding = new Padding(0);
                else Padding = new Padding(0, (Height - hi) / 2, 0, 0);
                var path = new GraphicsPath();
                int w = Width, h = Height, r = 20;
                path.AddArc(0, 0, r, r, 180, 90);
                path.AddArc(w - r, 0, r, r, 279, 90);
                path.AddArc(w - r, h - r, r, r, 0, 90);
                path.AddArc(0, h - r, r, r, 90, 90);
                path.CloseFigure();

                Region = new Region(path);
            };

        }
    }
}
