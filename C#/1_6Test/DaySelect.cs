using _1_6;
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
    public partial class DaySelect : UserControl {
        Point p1, p2;
        List<RadioButton> buttons = new List<RadioButton>();
        public DaySelect() {
            InitializeComponent();
            p1 = panel1.Location;
            p2 = panel2.Location;
            radioButton1.Select();
            int[] ints = { 1, 3, 5, 8, 12 };
            int n = 0;
            for(int y = 0; y < 2; y++) {
                for(int x = 0; x < 3; x++) {
                    RadioButton b = new RadioButton {
                        Text = ints[n] + "시간",
                        Appearance = Appearance.Button,
                        BackColor = sp.colors[sp.ReservationDetail],
                        ForeColor = Color.White,
                        TextAlign = ContentAlignment.MiddleCenter,
                        Margin = new Padding(10),
                        Dock = DockStyle.Fill,
                    };
                    buttons.Add(b);
                    tableLayoutPanel1.Controls.Add(b, x, y);
                    n++;
                    if(n == 5) { break; }
                }
            }

            buttons.ForEach(b => {
                b.CheckedChanged += (s, e) => {
                    price1.Text = "가격: " + (ints[buttons.IndexOf(b)] * 1500).ToString("N0") + "원";
                    price2.Text = price1.Text;
                    price1.Visible = true;
                    price2.Visible = true;
                    buttons.ForEach(bb => bb.BackColor = sp.colors[sp.ReservationDetail]);
                    b.BackColor = Color.LightGray;
                };
            });
        }

        private void radioButton2_CheckedChanged(object sender, EventArgs e) {
            panel2.Location = p1;
            panel1.Location = p2;
        }

        private void DaySelect_VisibleChanged(object sender, EventArgs e) {
            textBox1.Text = "";
            textBox2.Text = "";
            textBox3.Text = "";
            textBox4.Text = "";
            textBox5.Text = "";
            panel2.Location = p2;
            panel1.Location = p1;
            price1.Visible = false;
            price2.Visible = false;
            if(sp.user != null) myPoint.Text = "포인트 보유량: " + sp.user.point.ToString("N0")+ "pt";
            buttons.ForEach(b => b.Checked = false);
        }

        private void radioButton1_CheckedChanged(object sender, EventArgs e) {
            panel2.Location = p2;
            panel1.Location = p1;
        }
    }
}
