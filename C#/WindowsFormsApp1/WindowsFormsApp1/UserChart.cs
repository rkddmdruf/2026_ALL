using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Windows.Forms.DataVisualization.Charting;

namespace WindowsFormsApp1 {
    public partial class UserChart : UserControl {

        private Chart chart;
        private Series series;
        private ChartArea area;
        private Title chartTitle;
        public UserChart() {
            InitializeComponent();
            chart = chart1;

            chart.Titles.Clear();
            chart.Series.Clear();
            chart.ChartAreas.Clear();

            chart.Dock = DockStyle.Fill;
            area = new ChartArea("MainArea");
            area.BackColor = Color.Transparent;
            area.AxisX.MajorGrid.Enabled = false;
            area.AxisX.LineColor = Color.Gainsboro;
            area.AxisY.MajorGrid.LineColor = Color.FromArgb(230, 230, 230);
            area.AxisY.LineColor = Color.Gainsboro;
            area.AxisY.Minimum = 0;
            area.AxisY.Maximum = double.NaN;
            area.AxisY.Interval = 0;
            area.AxisY.IntervalAutoMode = IntervalAutoMode.VariableCount;
            chart.ChartAreas.Add(area);

            chartTitle = new Title {
                Font = new Font("맑은 고딕", 10F, FontStyle.Bold),
                ForeColor = Color.FromArgb(40, 90, 160),
                Alignment = ContentAlignment.TopLeft,
                Docking = Docking.Top
            };
            chart1.Titles.Add(chartTitle);
            chart1.Legends.Clear();

            series = new Series {
                ChartType = SeriesChartType.Column,
                Color = Color.FromArgb(0, 120, 215),
                IsValueShownAsLabel = true,
                LabelForeColor = Color.Black,
                Font = new Font("맑은 고딕", 9F, FontStyle.Bold)
            };
            
            chart1.Series.Add(series);
            foreach (Series s in chart1.Series) {
                s.IsValueShownAsLabel = true;
                s.LabelForeColor = Color.Black;
                s.Font = new Font("맑은 고딕", 9F, FontStyle.Bold);
            }

            chart1.DataBind();
        }

        public Color SeriesColor { set => series.Color = value; }
        public string ChartTitle { get => chartTitle.Text; set => chartTitle.Text = value; }
        public void AddData(string label, double value) {
            series.Points.AddXY(label, value);
        }
        public void ClearData() {
            series.Points.Clear();
        }
    }
}
