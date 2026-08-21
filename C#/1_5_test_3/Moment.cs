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
    public partial class Moment : Form {
        DateTime date = DateTime.Now;
        public Moment() {
            InitializeComponent();
            for (int i = 1; i <= 42; i++) {
                tableLayoutPanel1.Controls["mCard" + i].Margin = new Padding(0);
            }
            reload();
        }

        private void reload() {
            DateTime fDate = new DateTime(date.Year, date.Month, 1);
            momentLabel.Text = fDate.ToString("MM") + "월";
            int gap = (int)fDate.DayOfWeek;
            for (int i = 1; i <= 42; i++) {
                var p = (mCard)tableLayoutPanel1.Controls["mCard" + i];
                int day = i - gap;
                if (i <= gap || day > DateTime.DaysInMonth(fDate.Year, fDate.Month)) p.Visible = false;
                else p.Visible = true;
                p.label1.Text = day.ToString();
            }
        }

        private void left_Click(object sender, EventArgs e) {
            right.Enabled = true;
            date = new DateTime(date.Year, date.Month - 1, 1);
            if (date.Month <= DateTime.Now.Month) left.Enabled = false;
            reload();
        }

        private void right_Click(object sender, EventArgs e) {
            left.Enabled = true;
            date = new DateTime(date.Year, date.Month + 1, 1);
            if (date.Month >= 12) right.Enabled = false;
            reload();
        }
    }
}
