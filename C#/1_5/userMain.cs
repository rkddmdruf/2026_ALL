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
    public partial class userMain : UserControl {
        public userMain() {
            InitializeComponent();
            label2.Click += (s, e) => { 
                Parent.Hide();
                new Search().ShowDialog();
                Parent.Show();
            };
            label3.Click += (s, e) => { };
            label4.Click += (s, e) => { };
            label5.Click += (s, e) => { };
        }

        public FlowLayoutPanel inforPanel { get => panel1; }
    }
}
