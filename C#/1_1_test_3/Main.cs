using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_3 {
    public partial class Main : Form {
        string[] names = "대시보드,행사장 구조,부스 배치,타임테이블,공연자,스태프,티켓 판매,입점 업체,통계 리포트,설정".Split(',');
        Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        public Main() {
            InitializeComponent();
            panels.Add("대시보드", new DashBoard());
            panels.Add("통계 리포트", new Report());
            panels.Add("공연자", new PerformerForm());
            panels.Values.ToList().ForEach(x => panel3.Controls.Add(x));
            sp.panels = panels;
            sp.Show("통계 리포트");
        }

        private void label3_Click(object sender, EventArgs e) {
            Close();
        }

        private void label4_Click(object sender, EventArgs e) {
            WindowState = FormWindowState.Minimized;
        }

        private void Main_Load(object sender, EventArgs e2) {
            for (int i = 0; i < names.Count(); i++) {
                var b = (RadioButton)tableLayoutPanel1.Controls["radioButton" + (i + 1)];
                b.Text = names[i];  b.BackColor = Color.Transparent;
                b.FlatAppearance.BorderSize = 0;
                b.CheckedChanged += (s, e) => {
                    b.BackColor = b.Checked ? sp.setA(SystemColors.MenuHighlight) : Color.Transparent;
                    if(b.Checked) { sp.Show(b.Text); }
                };
            }
        }
    }
}
