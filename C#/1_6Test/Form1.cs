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
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();
            Icon = Icon.FromHandle(Properties.Resources.logo.GetHicon());
            dateLabel.Text = DateTime.Now.ToString("현재날짜: yyyy-MM-dd(dddd) hh:mm");
        }

        private void pictureBox1_Paint(object sender, PaintEventArgs e) {
            Graphics g = e.Graphics;
            g.FillEllipse(Brushes.Red, 131, 141, 15, 15);
        }
    }
}
