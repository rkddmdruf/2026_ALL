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
    public partial class DashBoard : UserControl {
        List<Color> colors = new List<Color> { SystemColors.Highlight, Color.RoyalBlue, Color.Chocolate, Color.ForestGreen };
        List<Color> colors2 = new List<Color> { SystemColors.Highlight, Color.Chocolate, Color.ForestGreen, Color.RoyalBlue};
        public DashBoard() {
            InitializeComponent();

            setChart();
            setCard();
            
        }

        private void setCard() {
            string[] str = "오늘 티켓 판매,누적 매출,진행 공연,배치 부스".Split(',');
            string[] infor = {
                sp.entity.TicketType.ToList().Select(t => t.Sold).Sum() + "매",
                "\\" + sp.entity.TicketType.ToList().Select(t => t.Sold * t.Price).Sum().ToString("N0"),
                sp.entity.EventItem.ToList().Where(t => getTime(t.StartHour) <= DateTime.Now.TimeOfDay && getTime(t.EndHour) > DateTime.Now.TimeOfDay).Count() +
                " / " + sp.entity.EventItem.ToList().Count(),
                sp.entity.Booth.ToList().Count() + " / 50",
            };

            for (int i = 0; i < 4; i++) {
                var p = ((DashCard)tableLayoutPanel2.Controls["dashCard" + (i + 1)]);
                p.l1.Text = str[i];
                p.l2.Text = infor[i];
                p.color = colors2[i];
            }
        }

        private TimeSpan getTime(decimal de) {
            double d = (double)de;
            int h = (int)d;
            return new TimeSpan(h, (int)(Math.Round(d * 60)), 0);
        }
        private void setChart() {
            for(int i = 0; i < 4; i++) {
                ((userChart)tableLayoutPanel1.Controls["userChart" + (i + 1)]).setColor = colors[i];
            }
            userChart1.ClearData();
            userChart2.ClearData();
            userChart3.ClearData();
            userChart4.ClearData();

            List<int> ints = new List<int> { 2, 4, 3, 1 };
            sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList().ForEach(t => userChart1.AddData(t.Name, t.Sold));

            List<string> boothNames = new List<string>("푸드,게임,안내,스테이지,스폰서,휴게".Split(','));
            sp.entity.BoothType.ToList().OrderBy(t => boothNames.IndexOf(t.Name)).ToList().ForEach(t => userChart2.AddData(t.Name, t.Booth.Count));

            sp.entity.Stage.ToList().OrderBy(t => t.SortOrder).ToList().ForEach(t => userChart3.AddData(t.Name.Split(' ')[0], t.EventItem.Count));

            List<string> staffs = new List<string>("무대,티켓,안전,운영,미디어".Split(','));
            List<string> staffsJob = new List<string>("stage,ticket,safety,ops,media".Split(','));
            var staffList = sp.entity.Staff.ToList();
            for (int s = 0; s < staffs.Count; s++) {
                userChart4.AddData(staffs[s], staffList.Where(t => staffsJob[s].Equals(t.Role)).ToList().Count);
            }
        }


    }
}
