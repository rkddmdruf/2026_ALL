using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Net.Mime.MediaTypeNames;
using Image = System.Drawing.Image;

namespace _1_6_test_2 {
    internal class RoundButton : Button {

        public RoundButton(Image img, string str, Color color) {

            Dock = DockStyle.Fill;
            FlatStyle = FlatStyle.Flat;
            FlatAppearance.BorderSize = 0;
            Region = null;

            Font = sp.f(10);
            ForeColor = Color.White;

            if (img != null) Image = sp.changeImage(img, new Size(60, 60), Color.White);
            Text = str;
            BackColor = color;

            TextImageRelation = TextImageRelation.ImageAboveText;
            ImageAlign = ContentAlignment.TopCenter;
            TextAlign = img == null ? ContentAlignment.MiddleCenter : ContentAlignment.BottomCenter;
            Resize += (s, e) => {
                int height = (int)(Font.Size * 1.4 + 10 + Image?.Height ?? 0);
                Padding = new Padding(0, (Height - height) / 2, 0, 0);
                if (img == null) Padding = new Padding(0);
                var path = new System.Drawing.Drawing2D.GraphicsPath();
                int r = 20, w = Width, h = Height;
                path.AddArc(w - r, h - r, r, r, 0, 90);
                path.AddArc(0, h - r, r, r, 90, 90);
                path.AddArc(0, 0, r, r, 180, 90);
                path.AddArc(w - r, 0, r, r, 270, 90);
                path.CloseFigure();
                Region = new Region(path);
            };
        }
    }
}
