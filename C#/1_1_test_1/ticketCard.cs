using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_1 {
    public partial class ticketCard : UserControl {
        public int value = 0, max = 0;
        public ticketCard() {
            InitializeComponent();
        }

        public Label nameLabel { get => label2; }
        public Label countLabel { get => label1; }
        public Label priceLabel { get => price; }

        private void panel1_Paint(object sender, PaintEventArgs e) {
            Graphics g = e.Graphics;
            using (Brush b = new SolidBrush(SystemColors.MenuHighlight)) {
                float f = (float)value / max;
                g.FillRectangle(Brushes.LightGray, 0, 0, panel1.Width, panel1.Height);
                g.FillRectangle(b, 0, 0, panel1.Width * f, panel1.Height);
            }
        }

        public Label countAll {  get => label4; }
    }

}
