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
    public partial class PerformerCard : UserControl {
        public PerformerCard() {
            InitializeComponent();
        }

        private void name_TextChanged(object sender, EventArgs e) {
            label.Text = name.Text.Length == 0 ? "" : name.Text.Substring(0, 1);
        }
        public Button b1 { get => button1; }
        public Button b2 { get => button2; }
        public Label statusLabel { get => status; }
        public Label nameLabel { get => name; }
        public Label label1 { get => infor1; }
        public Label label2 { get => infor2; }
    }
}
