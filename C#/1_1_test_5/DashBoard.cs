using _1_1_test_5;
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
    public partial class DashBoard : UserControl {
        public DashBoard() {
            InitializeComponent();
            userChart1.setTitle = "티켓 종류별 판매량 (매)";
            userChart1.setColor = SystemColors.MenuHighlight;

            userChart2.setTitle = "부스 유형별 배치수";
            userChart2.setColor = Color.RoyalBlue;

            userChart3.setTitle = "스테이지별 공연 수";
            userChart3.setColor = Color.Chocolate;

            userChart4.setTitle = "스태프 역할별 인원";
            userChart4.setColor = Color.ForestGreen;
            Color[] colors = { SystemColors.MenuHighlight, Color.ForestGreen, Color.Chocolate, Color.RoyalBlue};
            for (int i = 0; i < 4; i++) {
                ((dashCard)tableLayoutPanel2.Controls["dashCard" + (i + 1)]).color = colors[i];
            }
            reloadAll();

        }

        private void reloadAll() {
            for (int i = 0; i < 4; i++) {
                ((userChart)tableLayoutPanel1.Controls["userChart" + (i + 1)]).ClearData();
            }
            List<int> ints = new List<int> { 2, 4, 3, 1 };
            sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList().ForEach(t => userChart1.AddData(t.Name, t.Sold));

            List<string> str1 = new List<string>("푸드,게임,안내,스테이지,스폰서,휴게".Split(','));
            sp.entity.Booth.ToList().GroupBy(t => t.BoothType.Name).Select(t => new { key = t.Key, value = t.Count() }).OrderBy(t => str1.IndexOf(t.key))
                .ToList().ForEach(t => userChart2.AddData(t.key, t.value));
            List<string> str2 = new List<string>("메인,서브,야외,토크홀".Split(','));
            sp.entity.EventItem.ToList().GroupBy(t => t.Stage.Name).Select(t => new { key = t.Key, value = t.Count() }).OrderBy(t => str2.IndexOf(t.key))
                .ToList().ForEach(t => userChart3.AddData(t.key.Split(' ')[0], t.value));
            List<string> str3_1 = new List<string>("무대,티켓,안전,운영,미디어".Split(','));
            List<string> str3_2 = new List<string>("stage,ticket,safety,ops,media".Split(','));
            List<int> test = new List<int> { 0, 0, 0, 0, 0 };
            sp.entity.Staff.ToList().ForEach(t => test[str3_2.IndexOf(t.Role)]++);
            for (int i = 0; i < test.Count; i++)
                userChart4.AddData(str3_1[i], test[i]);

            List<string> cardString = new List<string>("오늘 티켓 판매,누적 매출,진행 공연,배치 부스".Split(','));
            List<string> value = new List<string> {
                sp.entity.TicketType.ToList().Select(t => t.Sold).Sum().ToString("N0") + "매",
                "\\ " + sp.entity.TicketType.ToList().Select(t => t.Sold * t.Price).Sum().ToString("N0"),
                sp.entity.EventItem.ToList()
                .Where(t => getTime(t.StartHour) <= DateTime.Now.TimeOfDay && getTime(t.EndHour) >= DateTime.Now.TimeOfDay).ToList().Count + " / " + sp.entity.EventItem.ToList().Count,
                sp.entity.Booth.ToList().Count + " / 50"
            };
            for (int i = 0; i < cardString.Count; i++) {
                var d = (dashCard)tableLayoutPanel2.Controls["dashCard" + (i + 1)];
                d.l1.Text = cardString[i];
                d.l2.Text = value[i];
            }
        }

        private TimeSpan getTime(decimal d) {
            int h = (int)d;
            decimal f = d - h;
            return new TimeSpan(h, (int)(Math.Round(f * 60)), 0);
        }
    }
}
