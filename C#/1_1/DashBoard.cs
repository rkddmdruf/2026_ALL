using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using WindowsFormsApp1;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace _1_1 {
    public partial class DashBoard : UserControl {
        string[] titles = "티켓 종류별 판매량 (매),부스 유형별 배치 수,스테이지별 공연 수,스태프 역할별 인원".Split(',');
        Color[] colors = { Color.DodgerBlue, Color.SteelBlue, Color.Chocolate, Color.ForestGreen };
        public DashBoard() {
            InitializeComponent();
            for (int i = 0; i < 4; i++) {
                var c = ((UserChart)tableLayoutPanel1.Controls["userChart" + (i + 1)]);
                c.ChartTitle = titles[i];
                c.SeriesColor = colors[i];
            }

            var cardTitles = "오늘 티켓 판매,누적 매출,진행 공연,배치 부스".Split(',');
            var cardInfor = new string[] { 
                sp.entity.TicketType.ToList().Sum(t => t.Sold) + "매",
                "\\" + sp.entity.TicketType.ToList().Sum(t => t.Sold * t.Price).ToString("N0"),
                "1 / 10",
                sp.entity.Booth.ToList().Count() + " / 50"
            };

            for (int i = 0; i < 4; i++) {
                Panel p = new Panel() {
                    Padding = new Padding(10, 10, 10, 15),
                    BackColor = Color.White,
                    BorderStyle = BorderStyle.FixedSingle
                };
                Color color = colors[i];
                p.Paint += (sender, e) => {
                    Graphics g = e.Graphics;
                    using (Brush b = new SolidBrush(color)) {
                        g.FillRectangle(b, 0, 0, 5, p.Height);
                    }
                };
                p.Controls.Add(new Label {
                    Text = cardTitles[i],
                    ForeColor = Color.Gray,
                    Dock = DockStyle.Top
                });
                p.Controls.Add(new Label {
                    Text = cardInfor[i],
                    AutoSize = false,
                    Dock = DockStyle.Fill,
                    Font = sp.f(18),
                    TextAlign = ContentAlignment.MiddleLeft,
                });
                tableLayoutPanel2.Controls.Add(p);
            }


            setChart1();
            setChart2();
            setChart3();
            setChart4();
        }

        private void setChart1() {
            List<int> id = new List<int> { 2, 4, 3, 1 };
            sp.entity.TicketType.ToList().OrderBy(t => id.IndexOf(t.Id)).ToList().ForEach(t => userChart1.AddData(t.Name, t.Sold));
        }

        private void setChart2() {
            List<string> boothNames = new List<string>("푸드,게임,안내,스테이지,스폰서,휴게".Split(','));
            sp.entity.BoothType.ToList().OrderBy(t => boothNames.IndexOf(t.Name)).ToList().ForEach(t => userChart2.AddData(t.Name, t.Booth.Count));
        }
        private void setChart3() {
            sp.entity.Stage.ToList().OrderBy(t => t.SortOrder).ToList().ForEach(t => userChart3.AddData(t.Name.Split(' ')[0], t.EventItem.Count));
        }
        private void setChart4() {
            List<string> staffs = new List<string>("무대,티켓,안전,운영,미디어".Split(','));
            List<string> staffsJob = new List<string>("stage,ticket,safety,ops,media".Split(','));
            var staffList = sp.entity.Staff.ToList();
            for (int s = 0; s < staffs.Count; s++) {
                userChart4.AddData(staffs[s], staffList.Where(t => staffsJob[s].Equals(t.Role)).ToList().Count);
            }
        }
    }
}
