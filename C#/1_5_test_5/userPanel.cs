using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_5 {
    public partial class userPanel : UserControl {
        public userPanel() {
            InitializeComponent();
        }
        public FlowLayoutPanel fp { get => flowLayoutPanel1; }

        private void label2_Click(object sender, EventArgs e) {
            if (sp.user == null) {
                userCheck();
                return;
            }
            this.Parent.Parent.Hide();
            new Search().ShowDialog();
            this.Parent.Parent.Show();
        }

        private void label3_Click(object sender, EventArgs e) {
            if (sp.user == null) {
                userCheck();
                return;
            }
            this.Parent.Parent.Hide();
            new ReservationForm(1).ShowDialog();
            this.Parent.Parent.Show();
        }

        private void label5_Click(object sender, EventArgs e) {
            if(sp.user == null) {
                userCheck();
                return;
            }
            this.Parent.Parent.Hide();
            new ReviewForm().ShowDialog();
            this.Parent.Parent.Show();
        }

        private void userCheck() {
            sp.err("로그인 후 이용가능합니다.");
            this.Parent.Parent.Hide();
            new Login().ShowDialog();
            this.Parent.Parent.Show();
        }
    }
}
