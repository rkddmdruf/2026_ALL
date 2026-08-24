using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Runtime.ConstrainedExecution;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {
    public partial class Moment : Form {
        DateTime date = DateTime.Now;
        hotel hotel;
        roomtype roomtype;
        double plusPrice;


        List<DateTime> dates;

        string[] dn = "일,월,화,수,목,금,토".Split(',');
        public Moment(int hno, int rtno, List<DateTime> returnDates) {
            dates = returnDates;

            hotel = sp.entity.hotel.ToList().Find(t => t.hno == hno);
            roomtype = sp.entity.roomtype.ToList().Find(t => t.rtno == rtno);
            plusPrice = hotel.rating.percentage.Value;

            InitializeComponent();
            p1.CellBorderStyle = TableLayoutPanelCellBorderStyle.Single;
            clearPanel();
            settingMoment();
            left.Enabled = false;
            hotelLabel.Text = hotel.hName + "(" + roomtype.rtname + " 1박 : " + (roomtype.baseprice.Value * plusPrice).ToString("N0") + "원)";
        }
        
        private void settingMoment() {
            DateTime first = new DateTime(date.Year, date.Month, 1);
            for (int i = 0; i < dn.Length; i++) {
                p1.Controls.Add(new Label {
                    Text = dn[i],
                    ForeColor = i == 0 || i == 6 ? Color.Red : Color.Black,
                    TextAlign = ContentAlignment.MiddleCenter,
                });
            }


            for(int i = 0; i < (int) first.DayOfWeek; i++) {
                p1.Controls.Add(new Label());
            }

            for (int i = 0; i < DateTime.DaysInMonth(date.Year, date.Month); i++) {
                Label l1 = new Label {
                    ForeColor = (int)first.DayOfWeek == 0 || (int)first.DayOfWeek == 6 ? Color.Red : Color.Black,
                    TextAlign = ContentAlignment.TopLeft,
                    Text = (i + 1).ToString(),
                    Dock = DockStyle.Top,
                    AutoSize = true,
                };
                Label l2 = new Label {
                    Dock = DockStyle.Top,
                    Text = "10실",
                    TextAlign = ContentAlignment.TopCenter,
                    ForeColor = (int)first.DayOfWeek == 0 || (int)first.DayOfWeek == 6 ? Color.Red : Color.Black,
                };
                Panel p = new Panel { Padding = new Padding(4, 4, 4, 4), Dock = DockStyle.Fill};
                p.Controls.Add(l2);
                p.Controls.Add(l1);
                p1.Controls.Add(p);

                DateTime d = first;
                if(first < DateTime.Today) { p.Enabled = false; }
                else if(dates.Count == 0) { dates.Add(d); p.BackColor = Color.Yellow; }

                EventHandler ck = (s, e) => {
                    if (p.BackColor == Color.Yellow) return;
                    p.BackColor = Color.Yellow;
                    dates.Add(d);
                    hotelLabel.Text = hotel.hName + 
                    "(" + roomtype.rtname + " " + dates.Count + "박 : " + 
                    ((roomtype.baseprice.Value * plusPrice) * dates.Count).ToString("N0") + "원)";
                };

                l1.Click += ck;
                l2.Click += ck;
                p.Click += ck;

                first = first.AddDays(1);
            }
            Refresh();
            left.Enabled = true; right.Enabled = true;
        }

        private void clearPanel() {
            left.Enabled = false; right.Enabled = false;
            p1.Controls.Clear();
            p1.ColumnStyles.Clear();
            p1.RowStyles.Clear();

            p1.ColumnCount = 7;
            p1.RowCount = 7;

            p1.RowStyles.Add(new RowStyle(SizeType.Absolute, 20));
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    p1.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100f / 7));
                }
                p1.RowStyles.Add(new RowStyle(SizeType.Percent, 100f / 6));
            }

            label1.Text = date.ToString("MM") + "월";
        }

        private void left_Click(object sender, EventArgs e) {
            right.Enabled = true;
            date = date.AddMonths(-1);
            clearPanel();
            settingMoment();
            if (date.Month == DateTime.Today.Month) left.Enabled = false;
        }

        private void right_Click(object sender, EventArgs e) {
            left.Enabled = true;
            date = date.AddMonths(1);
            clearPanel();
            settingMoment();
            if (date.Month == 12) right.Enabled = false;
        }

        private void button1_Click(object sender, EventArgs e) {
            if (dates.Count == 0) {
                sp.err("예약 날짜를 1일 이상 선택하세요.");
                return;
            }

            dates.Sort((a, b) => a.CompareTo(b));
            var line = (dates.Last() - dates.First()).Days + 1 == dates.Count;
            if(!line) { sp.err("연속된 날짜가 아닙니다."); return; }
            Close();
        }
    }
}
