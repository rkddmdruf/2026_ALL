using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_5 {
    public partial class dashCard : UserControl {
        public dashCard() {
            InitializeComponent();
        }

        public Color color;
        public Label l1 { get => label1; }
        public Label l2 { get => label2; }

        private void dashCard_Paint(object sender, PaintEventArgs e) {
            if (color == null) return;
            Graphics g = e.Graphics;
            using (Brush b = new SolidBrush(color))
                g.FillRectangle(b, 0, 0, 5, Height);
        }
    }
}
