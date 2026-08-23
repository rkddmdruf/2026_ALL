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
    public partial class PerformerCard : UserControl {
        public PerformerCard() {
            InitializeComponent();
        }

        public Label name1Label { get => name1; }
        public Label nameLabel { get => name; }
        public Label statusLabel { get => status; }
        public Label inforLabel1 { get => infor1; }
        public Label inforLabel2 { get => infor2; }

        public Button b1 { get => button1; }
        public Button b2 { get => button2; }
    }
}
