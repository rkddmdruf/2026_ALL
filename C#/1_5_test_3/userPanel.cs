using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_3 {
    public partial class userPanel : UserControl {
        public userPanel() {
            InitializeComponent();
        }

        private void label1_Click(object sender, EventArgs e) {
            this.Parent.Parent.Hide();
            new Search().ShowDialog();
            this.Parent.Parent.Show();
        }

        public FlowLayoutPanel fp { get => flowLayoutPanel1; }
    }
}
