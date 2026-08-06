using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6 {
    internal class RoundButton : Button{
        public RoundButton(Image img, string text, Color color) {
            Dock = DockStyle.Fill;
            FlatStyle = FlatStyle.Flat;
            FlatAppearance.BorderSize = 0;
            Region = null;

            Font = sp.f(10);
            ForeColor = Color.White;

            if(img == null) Image = new Bitmap(Properties.Resources._1, new Size(1, 1));
            else Image = new Bitmap(img, new Size(60, 60));
            Text = text;
            BackColor = color;

            TextImageRelation = TextImageRelation.ImageAboveText;
            ImageAlign = ContentAlignment.TopCenter;
            TextAlign = ContentAlignment.BottomCenter;

            Resize += (s, e) => {
                int height = (int)(Font.Size * 1.4 + 10 + Image.Height);
                Padding = new Padding(0, (Height - height) / 2, 0, 0);

                var path = new System.Drawing.Drawing2D.GraphicsPath();
                int r = 20, w = Width, h = Height;
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
