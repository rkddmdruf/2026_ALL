using _1_6;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Net.Mime.MediaTypeNames;
using Image = System.Drawing.Image;

namespace _1_6Test {
    internal class RoundButton : Button {

        public RoundButton(Image img, String text, Color? color = null) {
            Dock = DockStyle.Fill;
            FlatStyle = FlatStyle.Flat;
            FlatAppearance.BorderSize = 0;
            Region = null;

            Font = sp.f(10);
            ForeColor = Color.White;

            if (img != null) Image = sp.changeImageColor(img, new Size(60, 60), Color.White);
            Text = text;
            BackColor = color ?? Color.White;

            TextImageRelation = TextImageRelation.ImageAboveText;
            ImageAlign = ContentAlignment.MiddleCenter;
            TextAlign = ContentAlignment.BottomCenter;

            Resize += (s, e) => {
                var path = new GraphicsPath();
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
