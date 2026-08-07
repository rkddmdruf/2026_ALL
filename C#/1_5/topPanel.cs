using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {
    public partial class topPanel : UserControl {
        public topPanel() {
            InitializeComponent();
            pictureBox1.Image = Properties.Resources.logo.ToBitmap();
        }

        private void topPanel_Load(object sender, EventArgs e) {
            label1.Text = this.Parent.Text;
            ((Form)this.Parent).Icon = Properties.Resources.logo;
        }
    }
}
