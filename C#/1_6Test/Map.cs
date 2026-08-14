using _1_6;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6Test {
    public partial class Map : UserControl {
        public Map() {
            InitializeComponent();
        }

        private void panel1_Paint(object sender, PaintEventArgs e) {
            Graphics g = e.Graphics;
            g.FillEllipse(Brushes.Red, 131, 141, 12, 12);

            sp.entity.seat.ToList().ForEach(s => {
                g.FillRectangle(Brushes.Brown, s.s_x, s.s_y, s.s_w, s.s_h);
            });
        }
    }
}
