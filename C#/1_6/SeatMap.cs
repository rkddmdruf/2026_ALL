using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6 {
    public partial class SeatMap : UserControl {
        readonly Point start = new Point(131, 141);
        public SeatMap() {
            InitializeComponent();
        }

        private void panel1_Paint(object sender, PaintEventArgs e) {
            Graphics g = e.Graphics;

            using(var b = new SolidBrush(Color.Red)) {

            }
        }
    }
}
