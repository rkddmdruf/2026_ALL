using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_1 {
    public partial class Main : Form {
        Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        string[] names = "대시보드,행사장 구조,부스 배치,타임테이블,공연자,스태프,티켓 판매,입점 업체,통계 리포트, 설정".Split(',');
        public Main() {
            InitializeComponent();
            for (int i = 1; i <= 10; i++) {
                
                RadioButton b = (RadioButton)tableLayoutPanel1.Controls["radioButton" + i];
                b.Text = names[i - 1];
                b.FlatStyle = FlatStyle.Flat;
                b.FlatAppearance.BorderSize = 0;
                b.CheckedChanged += (s, e) => {
                    if(b.Checked) { b.ForeColor = SystemColors.MenuHighlight; b.BackColor = sp.setA(SystemColors.MenuHighlight, 50); }
                    else { b.BackColor = SystemColors.Control; b.ForeColor = Color.Black; }

                    UserControl p;
                    if(panels.TryGetValue(b.Text, out p)) {
                        panels.Values.ToList().ForEach(t => t.Visible = false);
                        p.Visible = true;
                    }
                };
            }
            panels.Add("티켓 판매", new TicketForm());
            panels.Add("입점 업체", new Store());
            panels.Add("대시보드", new DashBoard());
            panels.Add("통계 리포트", new report());
            panels.Add("공연자", new PerformerForm());

            panels.Values.ToList().ForEach(t => panel4.Controls.Add(t));
            tableLayoutPanel1.Controls[9].Select();
        }

        private void label3_Click(object sender, EventArgs e) {
            Close();
        }

        private void label4_Click(object sender, EventArgs e) {
            WindowState = FormWindowState.Minimized;
        }

        private void button1_Click(object sender, EventArgs e) {
            if (sp.check("로그아웃 하시겠습니까?") != DialogResult.Yes) return;
            Close();
        }
    }
}
