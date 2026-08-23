using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_5 {

    public partial class Moment : UserControl {
        DateTime date = DateTime.Now;
        public Moment() {
            InitializeComponent();
            string[] strs = "토,월,화,수,목,금,일".Split(',');
            for (int i = 0; i < 7; i++) {
                tableLayoutPanel1.Controls["l" + (i + 1)].Text = strs[i];
            }

            for(int i = 1; i <= 42; i++) {
                var l = (Label)tableLayoutPanel2.Controls["label" + i];
                l.Click += (s, e) => {
                    comboBox1.Items.Clear();
                    textBox1.Text = new DateTime(date.Year, date.Month, int.Parse(l.Text)).ToString("yyyy-MM-dd");
                    sp.selectTime = new DateTime(date.Year, date.Month, int.Parse(l.Text));
                    int time = DateTime.Parse(textBox1.Text).Date == DateTime.Now.Date ? DateTime.Now.Hour + 1 : 9;
                    while (time != 24) {
                        comboBox1.Items.Add(time++.ToString());
                    }
                    Refresh();
                    comboBox1.SelectedIndex = 0;
                };
                l.Paint += (sender, e) => {
                    Graphics g = e.Graphics;
                    g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
                    if (!sp.selectTime.Date.Equals(new DateTime(1119,11,19).Date) && sp.selectTime.Day.ToString().Equals(l.Text)) {
                        using (var brush = new SolidBrush(Color.Orange)) {
                            int r = 10;
                            g.FillEllipse(brush, l.Width / 2 - r, l.Height / 2 - r, r * 2, r * 2);
                            var sf = new StringFormat {
                                Alignment = StringAlignment.Center,
                                LineAlignment = StringAlignment.Center,
                            };
                            brush.Color = Color.White;
                            g.DrawString(l.Text, l.Font, brush, l.ClientRectangle, sf);
                        }
                    }
                };
            }
            textBox1.BackColor = Color.White;
            reload();
        }

        private void reload() {
            DateTime fDate = new DateTime(date.Year, date.Month, 1);
            int gap = (int)fDate.DayOfWeek;
            momentLabel.Text = fDate.ToString("MM월");
            for (int i = 1; i <= 42; i++) {
                var l = (Label) tableLayoutPanel2.Controls["label" + i];
                int day = i - gap;
                l.Visible = true;
                l.Enabled = true; ;
                if (i <= gap || day > DateTime.DaysInMonth(fDate.Year, fDate.Month)) 
                    l.Visible = false;
                if ((day >= 1 && day <= DateTime.DaysInMonth(fDate.Year, fDate.Month)) && DateTime.Now.Date > new DateTime(fDate.Year, fDate.Month, i - gap).Date)
                    l.Enabled = false;
                l.Text = day.ToString();
            }
        }

        private void left_Click(object sender, EventArgs e) {
            right.Enabled = true;
            date = date.AddMonths(-1);
            if (date.Month <= DateTime.Now.Month)
                left.Enabled = false;
            reload();
        }

        private void right_Click(object sender, EventArgs e) {
            left.Enabled = true;
            date = date.AddMonths(1);
            if (date.Month >= new DateTime(DateTime.Now.Year, 12, 31).Month)
                right.Enabled = false;
            reload();
        }

        private void label46_Click(object sender, EventArgs e) {
            sp.selectTime = new DateTime(sp.selectTime.Year, sp.selectTime.Month, sp.selectTime.Day, int.Parse(comboBox1.SelectedItem.ToString()), 0, 0);
            sp.Show("기간선택");
        }
    }
}
