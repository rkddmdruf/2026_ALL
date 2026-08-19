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
    public partial class report : UserControl {
        List<Color> colors2 = new List<Color> { Color.RoyalBlue, SystemColors.Highlight, Color.Chocolate, Color.ForestGreen };
        public report() {
            InitializeComponent();
        }

        private void report_VisibleChanged(object sender, EventArgs e) {
            if (!Visible) return;
            userChart1.ClearData();
            userChart2.ClearData();

            List<int> ints = new List<int> { 2, 4, 3, 1 };
            sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList().ForEach(t => userChart1.AddData(t.Name, t.Sold, colors2[t.Id - 1]));
            sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList().ForEach(t => userChart2.AddDataF(t.Name, (t.Sold * t.Price), colors2[t.Id - 1]));
        }
    }
}
