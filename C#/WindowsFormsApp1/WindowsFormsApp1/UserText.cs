using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1 {
    public partial class UserText : UserControl {
        public UserText() {
            InitializeComponent();

        }
        public TextBox tb { get => textBox1; }

        public Label lb { get => label1; }
        public Panel p { get => panel1; }
    }
}
