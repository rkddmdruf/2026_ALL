using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1
{
    public partial class TicketCard : UserControl
    {
        public int min = 0, max = 0;
        public TicketCard(int min, int max)
        {
            this.min = min;
            this.max = max;
            InitializeComponent();
        }

        public Label nameLabel { get => label1; }
        public Label priceLabel { get => price; }
        public Label ticketLabel1 { get => ticket1; }
        public Label ticketLabel2 { get => label2; }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;
            using (Brush b = new SolidBrush(SystemColors.MenuHighlight))
            {
                float f = (float)min / max;
                g.FillRectangle(Brushes.LightGray, 0, 0, panel1.Width, panel1.Height);
                g.FillRectangle(b, 0, 0, panel1.Width * f, panel1.Height);
            }
        }
    }
}
