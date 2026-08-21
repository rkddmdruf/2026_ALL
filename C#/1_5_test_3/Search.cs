using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_3 {
    public partial class Search : Form {
        public Search() {
            InitializeComponent();
            sp.setImage(this);
            Height = 330;
            comboBox1.Items.Add("전체");
            sp.entity.address.ToList().ForEach(t => comboBox1.Items.Add(t.aName));

            comboBox1.SelectedIndex = 0;
            comboBox2.SelectedIndex = 0;

            
        }

        private void reload() {
            tableLayoutPanel1.Controls.Clear();
            tableLayoutPanel1.RowStyles.Clear();
            tableLayoutPanel1.RowCount = 0;
            imgPanel.Controls.Clear();

            sp.entity.hotel.ToList()
                .Where(t => comboBox1.SelectedIndex == 0 || (comboBox1.SelectedItem.ToString().Equals(t.address.aName)))
                .Where(t => comboBox2.SelectedIndex == 0 || (comboBox2.SelectedItem.ToString().Substring(0, 1).Equals(t.ratno.ToString())))
                .Where(t => t.hName.Contains(textBox1.Text))
                .ToList().ForEach(t => {
                    Panel p = new Panel() {
                        Size = new Size((tableLayoutPanel1.Width - 100) / 7, 80),
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
                    Label l = new Label {
                        AutoSize = false,
                        Size = new Size(10, 13),
                        Image = new Bitmap(Properties.Resources.ping, new Size(10,13)),
                        Location = new Point(t.x.Value, t.y.Value),
                        BackColor = Color.Transparent,
                    };
                    l.Click += (s, e) => {
                        Hide();
                        new ReservationForm(t.hno).ShowDialog();
                        Show();
                    };
                    imgPanel.Controls.Add(l);
                    tableLayoutPanel1.Controls.Add(p);
                });
        }

        private void tableCount() {
            if (tableLayoutPanel1.Controls.Count == 0) {
                comboBox1.SelectedIndex = 0;
                comboBox2.SelectedIndex = 0;
                textBox1.Text = "";
                Height = 330;
                timer1.Stop();
                sp.err("검색결과가 없습니다.");
                reload();
            }
        }
        private void Search_Load(object ss, EventArgs ee) {
            reload();
            EventHandler ev = (s, e) => {
                reload();
                timer1.Start();
                tableCount();
            };

            comboBox1.SelectedIndexChanged += ev;
            comboBox2.SelectedIndexChanged += ev;
            textBox1.KeyDown += (s, e) => {
                if (e.KeyCode == Keys.Enter) {
                    reload();
                    timer1.Start();
                    tableCount();
                }
            };
        }

        private void timer1_Tick(object sender, EventArgs e) {
            Height += 3;
            if(Height >=1000) {
                Height = 1000;
                timer1.Stop();
            }
        }

        private void imgPanel_Paint(object sender, PaintEventArgs e) {
            Graphics g = e.Graphics;

            g.DrawImage(Properties.Resources.map,
                new Rectangle(0, 0, 900, 650),
                new Rectangle(25, 65, 550, 500), 
                GraphicsUnit.Pixel);
        }
    }
}
