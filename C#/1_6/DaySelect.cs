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

namespace _1_6 {
    public partial class DaySelect : UserControl {
        Point pointPoint, cardPoint;
        public DaySelect() {
            InitializeComponent();
            pointPoint = panel2.Location;
            cardPoint = panel3.Location;

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    tableLayoutPanel1.Controls.Add(new Button() {
                        Dock = DockStyle.Fill,
                        Margin = new Padding(10)
                    }, j, i);
                }
            }
            rb1.Select();
            rb1.BackColor = Color.Transparent;
            rb2.BackColor = Color.Transparent;
            seatSelect.BackColor = Color.Transparent;
            point.BackColor = Color.Transparent;
            pointLabel.BackColor = Color.Transparent;
            cardLabel.BackColor = Color.Transparent;
            priceLabel.BackColor = Color.Transparent;


            pointLabel.Text = "포인트 보유량: " + sp.user.point.ToString() + "pt";


            EventHandler onClick = (s, e) => {
                if((RadioButton) s ==  rb1) { 
                    panel2.Location = pointPoint;
                    panel3.Location = cardPoint;
                } else {
                    panel2.Location = cardPoint;
                    panel3.Location = pointPoint;
                }
            };

            textBox2.Enter += (s, e) => {
                // 이미 화상 키보드가 떠 있으면 다시 띄우지 않는다
                if (System.Diagnostics.Process.GetProcessesByName("osk").Length > 0) return;

                // osk.exe 는 매니페스트에 uiAccess 가 걸려 있어서 ShellExecute 로만 실행된다.
                // 그런데 이 프로그램은 32비트로 도는지라
                //   - UseShellExecute=true  + Sysnative 경로 -> ShellExecute 가 Sysnative 별칭을 못 읽음
                //   - UseShellExecute=false + Sysnative 경로 -> uiAccess 라서 "권한 상승 필요"로 거부
                //   - System32 경로         -> WOW64 리다이렉션으로 SysWOW64 를 보게 되어 파일 없음
                // 어느 쪽으로도 직접 실행이 안 되므로, 리다이렉션을 받지 않는 cmd 를 한 번 거쳐서
                // 그쪽에서 start 로 osk.exe 를 띄운다.
                string windir = Environment.GetFolderPath(Environment.SpecialFolder.Windows);
                string[] cmds = {
                    System.IO.Path.Combine(windir, @"Sysnative\cmd.exe"), // 64비트 OS + 32비트 프로세스일 때만 존재
                    System.IO.Path.Combine(windir, @"System32\cmd.exe")   // 64비트 프로세스 또는 32비트 OS
                };

                string error = "cmd.exe 를 찾을 수 없습니다.";
                foreach (string cmd in cmds) {
                    if (!System.IO.File.Exists(cmd)) continue;
                    try {
                        System.Diagnostics.Process.Start(new System.Diagnostics.ProcessStartInfo {
                            FileName = cmd,
                            Arguments = "/c start \"\" osk.exe",
                            UseShellExecute = false,
                            CreateNoWindow = true
                        });
                        return;
                    } catch (Exception ex) {
                        error = ex.Message; // 이 후보는 실패, 다음 후보로
                    }
                }
                MessageBox.Show("화상 키보드를 열 수 없습니다: " + error);
            };

            rb1.Click += onClick;
            rb2.Click += onClick;
        }

        protected override void OnLoad(EventArgs e) { }
    }
}
