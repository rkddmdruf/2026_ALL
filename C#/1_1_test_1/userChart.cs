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

namespace _1_1_test_1 {
    public partial class userChart : UserControl {
        Chart chart;
        Title title;
        ChartArea area;
        Series series;
        public userChart() {

            InitializeComponent();
            chart = chart1;
            chart.Series.Clear();
            chart.ChartAreas.Clear();
            chart.Titles.Clear();

            area = new ChartArea("MainArea");
            area.BackColor = Color.Transparent;
            area.AxisX.MajorGrid.Enabled = false;
            area.AxisX.LineColor = Color.Gainsboro;
            area.AxisY.MajorGrid.LineColor = Color.Gainsboro;
            area.AxisY.LineColor = Color.Gainsboro;
            area.AxisY.Minimum = 0;
            chart.ChartAreas.Add(area);

            title = new Title {
                Font = sp.fk(10),
                Alignment = ContentAlignment.TopLeft,
                Docking = Docking.Top
            };
            chart.Legends.Clear();
            chart.Titles.Add(title);

            series = new Series() {
                ChartType = SeriesChartType.Column,
                Color = SystemColors.MenuHighlight,
                IsValueShownAsLabel = true,
                LabelForeColor = Color.Black,
                Font = sp.fk(9),
            };
            chart.Series.Add(series);
        }

        public Color setColor { set => series.Color = value; }
        public string ChartTitle { set => title.Text = value; }
        public void AddDataF(string title, int value, Color color) {
            var p = series.Points[series.Points.AddXY(title, value)];
            p.Color = color;
            p.Label = "\\" + value.ToString("N0");
        }
        public void AddData(string title, object obj, Color color) {
            series.Points[series.Points.AddXY(title, obj.ToString())].Color = color;
        }
        public void AddData(string title, object obj) {
            series.Points.AddXY(title, obj.ToString());
        }
        public void ClearData() {
            series.Points.Clear();
        }
    }
}
