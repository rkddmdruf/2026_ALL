using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {

    public partial class CustomForm2 : Form {

        public CustomForm2() {
            InitializeComponent();
        }

        private void mapSet() {
            if (tableLayoutPanel1.Controls.Count == 0) {
                textBox1.Text = "";
                comboBox1.SelectedIndex = 0;
                comboBox2.SelectedIndex = 0;
                sp.err("검색결과가 없습니다.");
                Height = 350;
                return;
            }
            timer1.Start();
        }
        private void reload() {
            tableLayoutPanel1.Controls.Clear();

            
            sp.entity.hotel.ToList()
                //.Where(t => comboBox1.SelectedIndex == 0 || t.address.aName.Equals(comboBox1.SelectedItem.ToString()))
                .Where(t => comboBox2.SelectedIndex == 0 || t.ratno.Value.ToString().Equals(comboBox2.SelectedItem.ToString().Substring(0, 1)))
                .Where(t => t.hName.Contains(textBox1.Text))
                .Take(20)
                .ToList().ForEach(t => {
                    Panel p = new Panel {
                        BackgroundImage = Properties.Resources.ResourceManager.GetObject("_" + t.hno) as Bitmap,
                        BackgroundImageLayout = ImageLayout.Stretch,
                    };
                    p.Controls.Add(new Label {
                        Text = t.hName,
                        ForeColor = Color.Red,
                        BackColor = Color.Transparent,
                        Margin = new Padding(0, p.Height - 25, 0, 0),
                    });

                    Label l = new Label {
                        AutoSize = false,
                        Size = new Size(10, 12),
                        Location = new Point(t.x.Value, t.y.Value),
                        Image = Properties.Resources.ping,
                    };
                    l.Click += (s, e) => {
                        sp.infor(t.hName);
                    };
                    panel2.Controls.Add(l);

                    tableLayoutPanel1.Controls.Add(p);
                });
            
        }

        private void timer1_Tick(object sender, EventArgs e) {
            Height = Height + 1;
            if(Height >= 1000) {
                Height = 1000;
                timer1.Stop();
            }
        }

        private void panel2_Paint(object sender, PaintEventArgs e) {
            Graphics g = e.Graphics;
            g.DrawImage(Properties.Resources.map,
                new RectangleF(0, 0, 900, 650));
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e) {
            reload();
        }

        private void CustomForm2_Load(object sender, EventArgs e) {
           
    //
    //comboBox1.SelectedIndex = 1;
            comboBox2.SelectedIndex = 0;
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e) {
            reload();
        }

        private void tableLayoutPanel1_Paint(object sender, PaintEventArgs e) {

        }
    }
}
