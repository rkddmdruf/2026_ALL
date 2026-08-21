using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_4 {

    internal class RoundButton : Button {

        public RoundButton(Image img, string str, Color color) {
            FlatStyle = FlatStyle.Flat;
            FlatAppearance.BorderSize = 0;
            Region = null;
            Dock = DockStyle.Fill;

            if (img != null) Image = sp.changeImage(img, new Size(60, 60), Color.White);
            Font = sp.f(10);
            Text = str;
            ForeColor = Color.White;
            BackColor = color;

            this.TextImageRelation = TextImageRelation.ImageAboveText;
            this.TextAlign = img == null ? ContentAlignment.MiddleCenter : ContentAlignment.BottomCenter;
            this.ImageAlign = ContentAlignment.TopCenter;

            Resize += (s, e) => {
                if (img == null) Padding = new Padding(0);
                else { int hi = (int) (Font.Size * 1.4 + 10 + Image.Height); Padding = new Padding(0, (Height - hi) / 2, 0, 0); }
                int h = Height, w = this.Width, r = 20;
                GraphicsPath path = new GraphicsPath();
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
