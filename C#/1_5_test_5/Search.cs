using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_5 {
    public partial class Search : Form {
        public Search() {
            InitializeComponent();
            Size = new Size(916, 350);
            
            Icon = Properties.Resources.logo;
            titleImage.Image = Properties.Resources.logo.ToBitmap();
            comboBox1.Items.Clear();
            comboBox1.Items.Add("전체");
            sp.entity.address.ToList().ForEach(t => comboBox1.Items.Add(t.aName));
        }

        private void hotel5Panel1_Load(object sender, EventArgs e2) {
            comboBox1.SelectedIndex = 0;
            comboBox2.SelectedIndex = 0;

            comboBox1.SelectedIndexChanged += (s, e) => {
                reload();
                if(comboBox1.SelectedIndex !=0) timer1.Start();
                tableCount();
            };
            comboBox2.SelectedIndexChanged += (s, e) => {
                reload();
                if (comboBox2.SelectedIndex != 0) timer1.Start();
                tableCount();
            };
            reload();
        }

        private void reload() {
            tableLayoutPanel1.Controls.Clear();
            tableLayoutPanel1.RowCount = 0;
            tableLayoutPanel1.RowStyles.Clear();
            imgPanel.Controls.Clear();

            sp.entity.hotel.ToList()
                .Where(t => t.hName.Contains(textBox1.Text))
                .Where(t => comboBox1.SelectedIndex == 0 || sp.entity.address.ToList().First(c => c.aName.Equals(comboBox1.SelectedItem.ToString())).ano.Equals(t.ano))
                .Where(t => comboBox2.SelectedIndex == 0 || t.ratno.ToString().Equals(comboBox2.Text.Substring(0, 1)))
                .ToList()
                .ForEach(t => {
                    Panel p = new Panel() {
                        BackgroundImage = Properties.Resources.ResourceManager.GetObject("_" + t.hno) as Bitmap,
                        BackgroundImageLayout = ImageLayout.Stretch,
                        Dock = DockStyle.Fill,
                    };
                    p.Controls.Add(new Label {
                        Text = t.hName,
                        ForeColor = Color.Red,
                        BackColor = Color.Transparent,
                        AutoSize = false,
                        Dock = DockStyle.Fill,
                        Padding = new Padding(0, p.Height - 30, 0, 0)
                    });
                    tableLayoutPanel1.Controls.Add(p);

                    PictureBox pic = new PictureBox { Location = new Point(t.x.Value, t.y.Value), Image = Properties.Resources.ping, Size = new Size(10, 13), BackColor = Color.Transparent, SizeMode = PictureBoxSizeMode.StretchImage };
                    pic.Click += (s, e) => {
                        Hide();
                        new ReservationForm(t.hno).ShowDialog();
                        Show();
                    };
                    imgPanel.Controls.Add(pic);
                });
            tableLayoutPanel1.HorizontalScroll.Visible = false;
            
        }

        private void tableCount() {
            if(tableLayoutPanel1.Controls.Count == 0) {
                sp.err("검색 결과가 없습니다.");
                timer1.Stop();
                Height = 350;
                textBox1.Text = "";
                comboBox1.SelectedIndex = 0;
                comboBox2.SelectedIndex = 0;
                reload();
            }
        }

        private void panel3_Paint(object sender, PaintEventArgs e) {
            Graphics g = e.Graphics;
            g.DrawImage(Properties.Resources.map, new Rectangle(0, 0, 900, 650), new Rectangle(25, 55, 550, 550), GraphicsUnit.Pixel);
        }

        private void timer1_Tick(object sender, EventArgs e) {
            Height += 1;
            if(Height >= 1000) {
                Height = 1000;
                timer1.Stop();
            }
        }

        private void textBox1_KeyDown(object sender, KeyEventArgs e) {
            if(e.KeyCode == Keys.Enter) {
                reload();
                timer1.Start();
                tableCount();
            }
        }
    }
}
