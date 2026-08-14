using _1_6;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6Test {
    public partial class Form1 : Form {

        Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        public Form1() {
            InitializeComponent();
            sp.main = this;
            timeLabel.Text = DateTime.Now.ToString("현재날짜: yyyy-MM-dd(dddd) hh:mm");
            Icon = Icon.FromHandle(new Bitmap(Properties.Resources.logo).GetHicon());

            panels.Add("메인1", new Main1());
            panels.Add("메인2", new Main2());
            panels.Add("로그인", new Login());
            panels.Add("기간선택", new DaySelect());
            panels.Add("카드번호등록/수정", new Card());
            panels.Add("달력", new Moment());
            panels.Add("좌석배치도", new Map() {
                Dock = DockStyle.Fill,
            });

            foreach (var p  in panels.Values) {
                mainPanel.Controls.Add(p);
            }
            sp.panels = panels;
            sp.Show("좌석배치도");
            timer1.Start();
        }

        public Label beforeLabel { get => beforeLabels; }

        private void timer1_Tick(object sender, EventArgs e) {
            timeLabel.Text = DateTime.Now.ToString("현재날짜: yyyy-MM-dd(dddd) hh:mm");
        }

        private void beforeLabel_Click(object sender, EventArgs e) {
            if(sp.action.Count != 0) { sp.Show(sp.action.Pop()); }
        }
    }
}
