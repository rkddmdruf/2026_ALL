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
using System.Windows.Forms.DataVisualization.Charting;

namespace _1_1_test_5 {
    public partial class userChart : UserControl {
        Chart chart;
        Title title;
        ChartArea area;
        Series series;
        public userChart() {
            InitializeComponent();
            chart = chart1;

            chart.Series.Clear();
            chart.Titles.Clear();
            chart.ChartAreas.Clear();

            area = new ChartArea("MainArea");

            area.AxisX.MajorGrid.Enabled = false;
            area.AxisX.MajorGrid.LineColor = Color.Gainsboro;

            area.AxisY.MajorGrid.Enabled = true;
            area.AxisY.MajorGrid.LineColor = Color.Gainsboro;
            area.AxisY.Minimum = 0;
            area.AxisY.IntervalAutoMode = IntervalAutoMode.VariableCount;

            chart.ChartAreas.Add(area);
            chart.Legends.Clear();

            title = new Title() {
                Docking = Docking.Top,
                Alignment = ContentAlignment.TopLeft,
                Font = sp.f(10),
            };
            chart.Titles.Add(title);

            series = new Series() {
                ChartType = SeriesChartType.Column,
                IsValueShownAsLabel = true,
                LabelForeColor = Color.Black,
                Font = sp.f(10),
            };
            chart.Series.Add(series);
        }

        public Color setColor { set => series.Color = value; }
        public string setTitle { set => title.Text = value; }
        public void AddData(string label, object value) {
            series.Points.AddXY(label, value);
        }

        public void AddData(string label, object value, Color c) {
            series.Points[series.Points.AddXY(label, value)].Color = c;
        }
        public void AddDataF(string label, int value, Color c) {
            var p = series.Points[series.Points.AddXY(label, value)];
            p.Color = c;
            p.Label = "\\ " + value.ToString("N0");
        }

        public void ClearData() {
            series.Points.Clear();
        }
    }
}
