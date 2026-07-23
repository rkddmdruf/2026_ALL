using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Windows.Forms.DataVisualization.Charting;

namespace WindowsFormsApp1 {
    public partial class MainF : Form {
        List<Label> labels = new List<Label>();
        string[] menuNames = "대시보드,행사장 구조,부스 배치,타임테이블,공연자,스태프,티켓 판매,입점 업체,통계 리포트,설정".Split(',');
        Button logout = new Button() {
            Text = "로그아웃",
            BackColor = Color.White,
            FlatStyle = FlatStyle.Flat
        };
        Label userStatus = new Label() {
            Text = "관리자: 운영팀장",
            ForeColor = Color.Gray
        };

        public MainF() {
            InitializeComponent();
            TitleBar();
            Size = new Size(Size.Width, Size.Height + 32);
            buttonPanel.Controls.Add(userStatus);
            buttonPanel.Controls.Add(logout);

            bLabel.TextAlign = ContentAlignment.MiddleLeft;

            for (int i = 0; i < menuNames.Length; i++) {
                Label l = new Label() {
                    Text = menuNames[i],
                    AutoSize = false,
                    TextAlign = ContentAlignment.MiddleLeft,
                    Padding = new Padding(5, 5, 5, 5),
                };
                l.Click += (s, e) => {
                    foreach (var item in labels) {
                        item.BackColor = Color.Transparent;
                        item.ForeColor = SystemColors.ControlText;
                    }
                    l.BackColor = Color.SkyBlue;
                    l.ForeColor = Color.Blue;
                };
                labels.Add(l);
                buttonPanel.Controls.Add(l);
            }
            init();

            DashBoard b = new DashBoard() {
                Anchor = AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Top | AnchorStyles.Right,
                Dock = DockStyle.Fill,
                Size = new Size(panel1.Width, panel1.Height),
            };
            PerformerControl pc = new PerformerControl() {
                Anchor = AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Top | AnchorStyles.Right,
                Dock = DockStyle.Fill,
                Size = new Size(panel1.Width, panel1.Height),
            };
            panel1.Controls.Add(pc);
        }

        private void TitleBar() {
            Panel title = new Panel() {
                BackColor = Color.FromArgb(35, 35, 35),
                Dock = DockStyle.Top,
                Padding  = new Padding(16,8,16,8),
            };
            title.Size = new Size(this.Width, 32);
            Label l1 = new Label() {
                Text = "FM festival Manager",
                Font = getter.font,
                AutoSize = true,
                Dock = DockStyle.Left,
                ForeColor = Color.White,
                TextAlign = ContentAlignment.MiddleLeft   // ← 이거 추가
            };
            Label l2 = new Label() {
                Text = "        .2026 봄 캠퍼스 페스티벌",
                Font = getter.font,
                Dock = DockStyle.Left,
                AutoSize = true,
                ForeColor = Color.Gray,
                TextAlign = ContentAlignment.MiddleLeft   // ← 이거도 추가
            };

            Label dl = new Label() {
                Text = "⨉",
                AutoSize = true,
                ForeColor = Color.White,
                Font = getter.font,
                TextAlign = ContentAlignment.MiddleRight,
                Dock = DockStyle.Right,
            };
            Label sl = new Label() {
                Text = "—",
                AutoSize = true,
                ForeColor = Color.White,
                Font = getter.font,
                TextAlign = ContentAlignment.MiddleRight,
                Dock = DockStyle.Right,
            };
            Label after = new Label() {
                Text = "↷",
                Font = getter.font,
                ForeColor= Color.Black,
                AutoSize = true,
                Dock = DockStyle.Right,
            };
            Label before = new Label() {
                Text = "↶",
                Font = getter.font,
                ForeColor = Color.Black,
                AutoSize = true,
                Dock = DockStyle.Right,
            };
            Label space1 = new Label() {
                Size = new Size(20, 10),
                BackColor = Color.Transparent,
                Dock = DockStyle.Right,
            };
            Label space2 = new Label() {
                Size = new Size(20, 10),
                Dock = DockStyle.Right,
            };
            Label space3 = new Label() {
                Size = new Size(20, 10),
                Dock = DockStyle.Right,
            };

            title.Controls.Add(before);
            title.Controls.Add(space3);
            title.Controls.Add(after);
            title.Controls.Add(space2);
            title.Controls.Add(sl);
            title.Controls.Add(space1);
            title.Controls.Add(dl);
            title.Controls.Add(l2);
            title.Controls.Add(l1);

            dl.Click += (sender, e) => {
            };
            Controls.Add(title);
        }
        private void init() {
            int hgap = 32;
            int w = Width, h = Height;
            int sx = w / 5;
            buttonPanel.Location = new Point(0, hgap);
            buttonPanel.Size = new Size(sx, this.Size.Height);
            logout.Location = new Point(5, h - 30 - hgap);
            logout.Size = new Size(sx - 10, 25);
            userStatus.Location = new Point(7, h - 32 - userStatus.Font.Height - hgap);
            userStatus.Size = new Size(sx, userStatus.Font.Height);

            for (int i = 0; i < labels.Count; i++) {
                Label l = labels[i];
                l.Size = new Size(sx, 30);
                l.Location = new Point(0, i * 30);
            }
            panel1.Location = new Point(sx, hgap);
            panel1.Size = new Size(w - sx, h - 25 - hgap);

            bLabel.Location = new Point(sx, h - 25);
            bLabel.Size = new Size(w - sx, 25);
        }
        private void MainF_Resize(object sender, EventArgs e) {
            init();
        }
    }
}
