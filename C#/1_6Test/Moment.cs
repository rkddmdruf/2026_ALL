using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6Test {
    
    public partial class Moment : UserControl {
        string[] dayNames = "토,월,화,수,목,금,일".Split(',');
        DateTime now = DateTime.Now;
        public Moment() {
            InitializeComponent();
            foreach (var s in dayNames) {
                tableDayName.Controls.Add(new Label {
                    Text = s,
                    ForeColor = dayColor(s),
                    Dock = DockStyle.Fill,
                    TextAlign = ContentAlignment.MiddleCenter,
                });
            }
            reload();
        }

        private void reload() {
            DateTime oneDate = new DateTime(now.Year, now.Month, 1);
            tableLayoutPanel1.Controls.Clear();

            int s = (int) (oneDate.DayOfWeek + 1) % 7;
            for (int i = 0; i < s; i++) {
                tableLayoutPanel1.Controls.Add(new Label());
            }
            
            while(oneDate.Month == now.Month) {
                DateTime d = oneDate;
                Label l = new Label {
                    Text = oneDate.Day.ToString(),
                    Padding = new Padding(),
                    Margin = new Padding(),
                    ForeColor = dayColor(d),
                    TextAlign = ContentAlignment.MiddleCenter,
                };
                tableLayoutPanel1.Controls.Add(l);
                if (oneDate.Date < now.Date) l.Enabled = false;
                oneDate = oneDate.AddDays(1);
            }
        }
        private Color dayColor(DateTime d) {
            string s = d.ToString("ddd");
            return s.Equals("토") ? Color.Red : s.Equals("일") ? Color.Blue : Color.Black;
        }
        private Color dayColor(string s) {
            return s.Equals("토") ? Color.Red : s.Equals("일") ? Color.Blue : Color.Black;
        }
    }
}
