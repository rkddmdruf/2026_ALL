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
        Dictionary<String, UserControl> panels = new Dictionary<string, UserControl>();
        public MainForm() {
            InitializeComponent();
            button1.BackColor = Color.White;
            label2.Text = label2.Text + sp.entity.Festival.ToList()[0].Name;
            settingLeft();
            panels.Add("대시보드", new DashBoard());
            panels.Add("공연자", new PerformerControl());
            panels.Add("입점 업체", new Store());
            panels.Add("티켓 판매", new Tiket());

            panels.Values.ToList().ForEach(t =>
            {
                mainPanel.Controls.Add(t);
                t.Visible = false;
            });
            panels["티켓 판매"].Visible = true;
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

                int index = i;
                rb.BackColor = Color.Transparent;
                rb.FlatAppearance.BorderSize = 0;
                rb.CheckedChanged += (s, e) => {
                    rb.ForeColor = rb.Checked ? SystemColors.MenuHighlight : Color.Black;
                    rb.BackColor = rb.Checked ? sp.setA(SystemColors.MenuHighlight, 200) : SystemColors.Control;
                    var ts = new UserControl();
                    if(panels.TryGetValue(menuNames[index], out ts))
                    {
                        panels.Values.ToList().ForEach(x => x.Visible = false);
                        ts.Visible = true;
                    }
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
