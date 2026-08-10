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
        public Search() {
            InitializeComponent();
            Size = new Size(Size.Width, 350);
            tableLayoutPanel1.ColumnCount = 7;
            for (int i = 0; i < 7; i++) tableLayoutPanel1.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100f / 7));

            comboBox1.Items.Add("전체");
            sp.entity.address.ToList().ForEach(x => comboBox1.Items.Add(x.aName));
            comboBox2.Items.AddRange("전체,5성급,4성급,3성급,2성급,1성급".Split(','));

            comboBox1.SelectedIndex = 0; comboBox2.SelectedIndex = 0;
            textBox1.KeyDown += textBox1_KeyDown;

            setTablePanel();
            reloadTable();
        }

        private void reloadTable() {
            sp.entity.hotel.ToList()
                .Where(t => comboBox1.SelectedIndex == 0 || t.address.aName.Equals(comboBox1.SelectedItem.ToString()))
                .Where(t => comboBox2.SelectedIndex == 0 || t.ratno.Value.Equals(comboBox2.SelectedIndex))
                .Where(t => t.hName.Contains(textBox1.Text)).ToList().ForEach(t => {
                    Panel img = new Panel {
                        BackgroundImage = (Image)Properties.Resources.ResourceManager.GetObject("_" + t.hno),
                        Dock = DockStyle.Fill,
                        BackgroundImageLayout = ImageLayout.Stretch
                    };
                    Label label  = new Label { Text = t.hName, ForeColor = Color.Red, Dock = DockStyle.Bottom, Margin = new Padding(0, 0, 0, 10), BackColor = Color.Transparent };
                    img.Controls.Add(label);
                    tableLayoutPanel1.Controls.Add(img);
                });
        }

        private void setTablePanel() {
            tableLayoutPanel1.Controls.Clear();
            tableLayoutPanel1.RowStyles.Clear();
            tableLayoutPanel1.RowCount = 0;
        }

        private void textBox1_KeyDown(object sender, KeyEventArgs e) {
            if (e.KeyCode == Keys.Enter) {
                e.SuppressKeyPress = true;  // "딩!" 소리 제거
                comboBox1.SelectedIndex = 0; comboBox2.SelectedIndex = 0;
                setTablePanel();
            }
        }
    }
}
