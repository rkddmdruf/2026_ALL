using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5test1 {

    public partial class Search : Form {
        List<hotel> list = new List<hotel>();

        public Search() {
            InitializeComponent();
            Size = new Size(Width, 350);
            tableLayoutPanel1.Padding = new Padding(0, 0, 30, 0);
            comboBox1.Items.Add("전체");
            sp.entity.address.ToList().ForEach(a => {
                comboBox1.Items.Add(a.aName);
            });
            comboBox2.Items.Add("전체");
            for (int i = 5; i >= 1; i--) comboBox2.Items.Add(i.ToString() + "등급");


        }

        private void Search_VisibleChanged(object sender, EventArgs e) {
            if (!Visible) return;
            comboBox1.SelectedIndex = 0;
            comboBox2.SelectedIndex = 0;
            textBox1.Text = "";
            reload();
        }

        private void reload() {
            tableLayoutPanel1.Controls.Clear();
            tableLayoutPanel1.RowCount = 0;
            tableLayoutPanel1.RowStyles.Clear();

            imgPanel.Controls.Clear();

            list = sp.entity.hotel.ToList()
                .Where(t => comboBox1.SelectedIndex == 0 || comboBox1.SelectedItem.ToString().Equals(t.address.aName))
                .Where(t => comboBox2.SelectedIndex == 0 || comboBox2.SelectedItem.ToString().Substring(0, 1).Equals(t.ratno.ToString()))
                .Where(t => t.hName.Contains(textBox1.Text)).ToList();
            list.ForEach((t) => {
                Panel p = new Panel {
                    Dock = DockStyle.Fill,
                    BackgroundImage = Properties.Resources.ResourceManager.GetObject("_" + t.hno) as Image,
                    BackgroundImageLayout = ImageLayout.Stretch,
                };
                p.Controls.Add(new Label {
                    Text = t.hName,
                    ForeColor = Color.Red,
                    Dock = DockStyle.Bottom,
                    Padding = new Padding(0, 0, 0, 10),
                    BackColor = Color.Transparent
                });
                tableLayoutPanel1.Controls.Add(p);
                PictureBox pic = new PictureBox {
                    Size = new Size(10, 14),
                    Image = Properties.Resources.ping,
                    SizeMode = PictureBoxSizeMode.StretchImage,
                    Location = new Point(t.x.Value, t.y.Value),
                    BackColor = Color.Transparent,
                };
                pic.Click += (s, e) => {

                };
                imgPanel.Controls.Add(pic);
            });
            panel1.Refresh();
        }

        private void textBox1_KeyUp(object sender, KeyEventArgs e) {
            if (e.KeyCode == Keys.Enter) {
                reload();
                if(list.Count == 0) {
                    sp.err("검색 결과가 없습니다.");
                    comboBox1.SelectedIndex = 0;
                    comboBox2.SelectedIndex = 0;
                    textBox1.Text = "";
                    Size = new Size(Width, 350);
                    reload();
                    return;
                }
                timer1.Start(); 

            }
        }

        private void timer1_Tick(object sender, EventArgs e) {
            Size = new Size(Width, Height + 1);
            if(Height >= 1000) {
                Height = 1000;
                timer1.Stop();
            }
        }
    }
}
