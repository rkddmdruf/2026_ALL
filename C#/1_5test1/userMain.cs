using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5test1 {
    public partial class userMain : UserControl {
        public userMain() {
            InitializeComponent();
        }

        public FlowLayoutPanel flp { get => flowLayoutPanel1; }

        private void l1_Click(object sender, EventArgs e) {
            this.Parent.Parent.Hide();
            new Search().ShowDialog();
            this.Parent.Parent.Show();
        }

        private void l2_Click(object sender, EventArgs e) {
            this.Parent.Parent.Hide();
            new ReservationForm(1).ShowDialog();
            this.Parent.Parent.Show();
        }
    }
}
