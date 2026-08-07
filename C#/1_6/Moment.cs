using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Net.Mime.MediaTypeNames;

namespace _1_6 {
    public partial class Moment : UserControl {
        string[] dayName = "토,월,화,수,목,금,일".Split(',');
        DateTime now = DateTime.Now;
        Nullable<DateTime> selectDate = DateTime.Now;
        public Moment() {
            InitializeComponent();
            for (int i = 0; i < dayName.Length; i++) {
                dayNameGrid.Controls.Add(new Label {
                    Text = dayName[i],
                    Dock = DockStyle.Fill,
                    ForeColor = dayName[i].Equals("토") ? Color.Red : dayName[i].Equals("일") ? Color.Blue : Color.Black,
                    TextAlign = ContentAlignment.MiddleCenter
                });
            }

            EventHandler lrc = (s, e) => {
                left.Enabled = DateTime.Now.Date < now;
                now =  now.AddMonths(s == left ? -1 : 1);
                chageMoment();
            };
            left.Click += lrc;
            right.Click += lrc;

            settingTable();
            chageMoment();
            setComboBox();
        }


        private void settingTable() {
            tableLayoutPanel1.ColumnCount = 7;
            tableLayoutPanel1.RowCount = 6;

            tableLayoutPanel1.ColumnStyles.Clear();
            tableLayoutPanel1.RowStyles.Clear();

            for(int x = 0; x < tableLayoutPanel1.ColumnCount; x++) {
                tableLayoutPanel1.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100 / tableLayoutPanel1.ColumnCount));
            }
            for (int y = 0; y < tableLayoutPanel1.RowCount; y++) {
                tableLayoutPanel1.RowStyles.Add(new RowStyle(SizeType.Percent, 100 / tableLayoutPanel1.RowCount));
            }

            tableLayoutPanel1.CellBorderStyle = TableLayoutPanelCellBorderStyle.Inset;
        }

        public void chageMoment() {
            DateTime changeDate = new DateTime(now.Year, now.Month, 1);
            momentLabel.Text = now.Month + "월";
            tableLayoutPanel1.Controls.Clear();

            int sx = 0;
            for(int i = 0; i < ((int)changeDate.DayOfWeek + 1) % 7; i++) {
                tableLayoutPanel1.Controls.Add(new Label(), i, 0);
                sx++;
            }
            for (int y = 0; y < 6; y++) {
                for (sx = sx; sx < 7; sx++) {
                    int day = changeDate.Day;
                    DateTime newDate = new DateTime(changeDate.Year, changeDate.Month, changeDate.Day);
                    Label l = new Label() {
                        Text = day.ToString(),
                        ForeColor = sx == 0 ? Color.Red : sx == 6 ? Color.Blue : Color.Black,
                        TextAlign = ContentAlignment.MiddleCenter,
                    };
                    l.Paint += (sender, e) => {
                        Graphics g = e.Graphics;
                        g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
                        if (selectDate != null && selectDate.Value.Date == newDate.Date) {
                            using(var brush = new SolidBrush(Color.Orange)) {
                                int r = 10;
                                g.FillEllipse(brush, l.Width / 2 - r, l.Height / 2 - r, r*2, r*2);
                                var sf = new StringFormat {
                                    Alignment = StringAlignment.Center,
                                    LineAlignment = StringAlignment.Center,
                                };
                                brush.Color = Color.White;
                                g.DrawString(l.Text, l.Font, brush, l.ClientRectangle, sf);
                            }
                        }
                    };

                    EventHandler ac = (s, e) => selectAction(newDate);
                    l.Click += ac;
                    if (DateTime.Now.Date > changeDate.Date) l.Enabled = false;
                    if (selectDate.Value.Date == DateTime.Now.Date) selectAction(selectDate.Value);
                    tableLayoutPanel1.Controls.Add(l, sx, y);
                    changeDate = changeDate.AddDays(1);
                    if (changeDate.Month != now.Month) break;
                }
                sx = 0;
                if (changeDate.Month != now.Month) break;
            }

        }

        private void selectAction(DateTime newDate) {
            selectDate = newDate;
            selectDateLabel.Text = selectDate.Value.Date.ToString("yyyy-MM-dd");
            selectDateLabel.TextAlign = ContentAlignment.MiddleLeft;
            setComboBox();
            Refresh();
        }
        private void setComboBox() {
            comboBox1.Items.Clear();
            TimeSpan time = new TimeSpan(9, 0, 0);
            if (selectDate != null && selectDate.Value.Date == DateTime.Now.Date) 
                time = new TimeSpan(DateTime.Now.Minute == 0 ? DateTime.Now.Hour : DateTime.Now.Hour + 1, 0, 0);

            for (TimeSpan t = time; t <= new TimeSpan(0, 23, 0, 0); t = new TimeSpan(t.Hours + 1, 0, 0))
                comboBox1.Items.Add(t.ToString("hh"));
            comboBox1.SelectedIndex = 0;
        }

        private void nextLabel_Click(object sender, EventArgs e) {
            if (selectDate is null) {
                sp.err("날짜가 선택되지 않았습니다.");
                return;
            }
            if(comboBox1.SelectedIndex < 0) {
                sp.err("입장시간을 선택하지 않았습니다.");
                return;
            }
            DateTime datetime = selectDate.Value;
            sp.momentFormToSelectDate = new DateTime(datetime.Year, datetime.Month, datetime.Day, int.Parse(comboBox1.SelectedItem.ToString()), 0, 0);
            sp.Show("기간선택");
        }
    }
}
