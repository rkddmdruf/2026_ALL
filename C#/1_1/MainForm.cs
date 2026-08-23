using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using WindowsFormsApp1;
using static System.Windows.Forms.VisualStyles.VisualStyleElement.Button;
using RadioButton = System.Windows.Forms.RadioButton;

namespace _1_1 {
    public partial class MainForm : Form {
        string[] menuNames = "대시보드,행사장 구조,부스 배치,타임테이블,공연자,스태프,티켓 판매,입점 업체,통계 리포트,설정".Split(',');
        public MainForm() {
            InitializeComponent();
            button1.BackColor = Color.White;
            label2.Text = label2.Text + sp.entity.Festival.ToList()[0].Name;
            settingLeft();
            mainPanel.Controls.Add(new DashBoard());

        }

        private void settingLeft() {
            tableLayoutPanel1.RowStyles.Clear();
            tableLayoutPanel1.ColumnStyles.Clear();
            tableLayoutPanel1.RowCount = 10;
            for (int i = 0; i < 10; i++) {
                tableLayoutPanel1.RowStyles.Add(new RowStyle(SizeType.Percent, 10f));
                RadioButton rb = new RadioButton() {
                    Text = menuNames[i],
                    Appearance = Appearance.Button,
                    FlatStyle = FlatStyle.Flat,
                    TextAlign = ContentAlignment.MiddleLeft,
                    Dock = DockStyle.Fill,

                    Margin = new Padding(),
                };
                tableLayoutPanel1.Controls.Add(rb);

                rb.BackColor = Color.Transparent;
                rb.FlatAppearance.BorderSize = 0;
                rb.CheckedChanged += (s, e) => {
                    rb.ForeColor = rb.Checked ? SystemColors.MenuHighlight : Color.Black;
                    rb.BackColor = rb.Checked ? sp.setA(SystemColors.MenuHighlight, 200) : SystemColors.Control;
                };
            }
            tableLayoutPanel1.Controls[0].Select();
        }
        private void MainForm_Load(object sender, EventArgs e) {
            label2.Text = "· " + sp.entity.Festival.ToList()[0].Name;
        }

        private void button1_Click(object sender, EventArgs e) {
            if (MessageBox.Show("로그아웃 하시겠습니까?", "확인", MessageBoxButtons.YesNo, MessageBoxIcon.Question) != DialogResult.Yes) return;
            sp.user = null;
            Close();
        }

        private void label5_Click(object sender, EventArgs e) {
            Close();
        }

        private void label6_Click(object sender, EventArgs e) {
            WindowState = FormWindowState.Minimized;
        }
    }
}
