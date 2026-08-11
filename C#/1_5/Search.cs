using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {
    public partial class Search : Form {
        int dir = 1;
        List<hotel> list = new List<hotel>();
        public Search() {
            InitializeComponent();
            Size = new Size(Size.Width, 350);
            tableLayoutPanel1.ColumnStyles.Clear();
            tableLayoutPanel1.ColumnCount = 7;
            tableLayoutPanel1.Padding = new Padding(0, 0, SystemInformation.VerticalScrollBarWidth, 0);
            for (int i = 0; i < 7; i++) tableLayoutPanel1.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100f / 7));

            comboBox1.Items.Add("전체");
            sp.entity.address.ToList().ForEach(x => comboBox1.Items.Add(x.aName));
            comboBox2.Items.AddRange("전체,5성급,4성급,3성급,2성급,1성급".Split(','));

            comboBox1.SelectedIndex = 0; comboBox2.SelectedIndex = 0;
            textBox1.KeyDown += textBox1_KeyDown;

            EventHandler ev = (s, e) => {
                setTablePanel();
                reloadTable();
                tableCount();
            };
            comboBox1.SelectedIndexChanged += ev;
            comboBox2.SelectedIndexChanged += ev;
            setTablePanel();
            reloadTable();
        }

        private void reloadTable() {
            list = sp.entity.hotel.ToList()
                .Where(t => comboBox1.SelectedIndex == 0 || t.address.aName.Equals(comboBox1.SelectedItem.ToString()))
                .Where(t => comboBox2.SelectedIndex == 0 || t.ratno.Value == int.Parse(comboBox2.SelectedItem.ToString().Substring(0, 1)))
                .Where(t => t.hName.Contains(textBox1.Text)).ToList();
            list.ForEach(t => {
                Panel img = new Panel {
                    BackgroundImage = (Image)Properties.Resources.ResourceManager.GetObject("_" + t.hno),
                    Dock = DockStyle.Fill,
                    BackgroundImageLayout = ImageLayout.Stretch
                };
                Label label = new Label { Text = t.hName, ForeColor = Color.Red, Dock = DockStyle.Bottom, Margin = new Padding(0, 0, 0, 10), BackColor = Color.Transparent };
                img.Controls.Add(label);
                tableLayoutPanel1.Controls.Add(img);
            });
        }

        private void setTablePanel() {
            tableLayoutPanel1.Controls.Clear();
            tableLayoutPanel1.RowCount = 0;
            tableLayoutPanel1.RowStyles.Clear();
        }

        private void textBox1_KeyDown(object sender, KeyEventArgs e) {
            if (e.KeyCode == Keys.Enter) {
                e.SuppressKeyPress = true;  // "딩!" 소리 제거
                comboBox1.SelectedIndex = 0; comboBox2.SelectedIndex = 0;
                setTablePanel();
                reloadTable();
                tableCount();
            }
        }

        private void tableCount() {
            if (tableLayoutPanel1.Controls.Count == 0) {
                sp.err("검색 결과가 없습니다.");
                textBox1.Text = "";
                Size = new Size(Size.Width, 350);
                return;
            }
            timer1.Start();
        }
        private void timer1_Tick(object sender, EventArgs e) {
            Size = new Size(Size.Width, Size.Height + dir);
            if (Size.Height >= 1000) {
                Size = new Size(Size.Width, 1000);
                timer1.Stop();
            }
        }

        private void pictureBox1_Paint(object sender, PaintEventArgs e) {
            Graphics g = e.Graphics;
            g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

            g.DrawImage(Properties.Resources.map,
                new Rectangle(0, 0, 900, 650),
                new Rectangle(25, 65, 550, 550),
            GraphicsUnit.Pixel);
            list.ForEach(t => g.DrawImage(Properties.Resources.ping, t.x.Value, t.y.Value, 10, 12));
        }
    }
}
