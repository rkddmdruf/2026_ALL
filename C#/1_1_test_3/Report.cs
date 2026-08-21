using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_3 {
    public partial class Report : UserControl {
        List<Color> colors = new List<Color> { Color.RoyalBlue, SystemColors.MenuHighlight, Color.Chocolate, Color.ForestGreen};
        public Report() {
            InitializeComponent();
            userChart1.setTitle = "티켓 종류별 판매량 (매)";
            userChart2.setTitle = "티켓 종류별 매출(\\)";
        }

        private void Report_VisibleChanged(object sender, EventArgs e) {
            userChart1.ClearData();
            userChart2.ClearData();
            List<int> ints = new List<int> { 2, 4, 3, 1 };
            var list = sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList();

            list.ForEach(t => userChart1.AddData(t.Name, t.Sold, colors[t.Id - 1]));
            list.ForEach(t => userChart2.AddDataF(t.Name, t.Sold * t.Price, colors[t.Id - 1]));
        }
    }
}
