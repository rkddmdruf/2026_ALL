using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Templet_Test {
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();
            
        }
        public static void err(string msg) {
            MessageBox.Show(msg, "경고", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }
}
