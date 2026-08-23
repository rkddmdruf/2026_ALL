using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_5 {
    internal class RoundButton : Button {

        public RoundButton(Image img, string str, Color color) {
            Dock = DockStyle.Fill;
            FlatStyle = FlatStyle.Flat;
            FlatAppearance.BorderSize = 0;
            Region = null;

            if (img != null) { Image = sp.changeImage(img, new Size(60, 60), Color.White); }
            Text = str;
            Font = sp.f(10);
            ForeColor = Color.White;
            BackColor = color;

            TextImageRelation = TextImageRelation.ImageAboveText;
            TextAlign = ContentAlignment.TopCenter;
            ImageAlign = img == null ? ContentAlignment.MiddleCenter : ContentAlignment.BottomCenter;

            Resize += (s, e) => {
                int he = (int) (Font.Size * 1.4 + img?.Height ?? 0 + 10);
                if (img == null) Padding = new Padding(0);
                else Padding = new Padding(0, (Height - he) / 2, 0, 0);
                GraphicsPath path = new GraphicsPath();
                int w = Width; int h = Height; int r = 20;
                path.AddArc(0, 0, r, r, 180, 90);
                path.AddArc(w - r, 0, r, r, 270, 90);
                path.AddArc(w - r, h - r, r, r, 0, 90);
                path.AddArc(0, h - r, r, r, 90, 90);
                path.CloseFigure();
                Region = new Region(path);
            };
        }
    }
}
