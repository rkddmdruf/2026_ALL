using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1 {
    public partial class performerCard : UserControl {
        public performerCard() {
            InitializeComponent();
            button1.FlatStyle = FlatStyle.Flat;
            button1.FlatAppearance.BorderSize = 1;
            button1.FlatAppearance.BorderColor = Color.Gray;

            button2.FlatStyle = FlatStyle.Flat;
            button2.FlatAppearance.BorderSize = 1;
            button2.FlatAppearance.BorderColor = Color.Gray;
        }

        public Label l1 {  get => label1; }
        public Label l2 { get => label2; }
        public Label l3 { get => label3; }
        public Label l4 { get => label4; }

        public Label l5 { get => label5; }

        public Button b1 { get => button1;}
        public Button b2 { get => button2; }

        private void performerCard_Load(object sender, EventArgs e) {
            label1.TextAlign = ContentAlignment.MiddleCenter;
        }
    }
}
