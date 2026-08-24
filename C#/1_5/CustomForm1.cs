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
    public partial class CustomForm1 : Form {
        public CustomForm1() {
            InitializeComponent();

            DateTime now = new DateTime(DateTime.Now.Year, DateTime.Now.Month, 1);
            int gap = (int) now.DayOfWeek;
            int last = now.AddMonths(1).AddDays(-1).Day;
            for (int i = 1; i <= 42; i++) {
                MomentCard uc = ((MomentCard)tableLayoutPanel1.Controls["momentCard" + (i)]);
                uc.Visible = (i > gap && i <= gap + last);
                uc.label1.Text = (i - gap).ToString();
                uc.label1.ForeColor = i % 7 == 1 || i % 7 == 0 ? Color.Red : Color.Black;
                uc.label2.ForeColor = uc.label1.ForeColor;
                uc.Enabled = false;
                uc.label1.Enabled = false;
                uc.label2.Enabled = false;
            }

        }

        private void label4_Click(object sender, EventArgs e) {

        }
    }
}
