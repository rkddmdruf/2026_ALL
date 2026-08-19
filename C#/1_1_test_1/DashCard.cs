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
    public partial class DashCard : UserControl {
        public DashCard() {
            InitializeComponent();
        }

        public Label l1 { get => label1; }
        public Label l2 { get => label2; }

        public Color c;
        public Color color { get => c; set => c = value; }

        private void panel1_Paint(object sender, PaintEventArgs e) {
            Graphics g  = e.Graphics;
            if (color == null) { return; }
            using (Brush b = new SolidBrush(c)) 
                g.FillRectangle(b, 0, 0, 5, panel1.Height);
        }
    }
}
