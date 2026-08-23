using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_5 {
    public partial class Form1 : Form {
        public Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        
        public Form1() {
            InitializeComponent();
            
            panels.Add("메인1", new Main1());
            panels.Add("메인2", new Main2());
            panels.Add("로그인", new Login());
            panels.Add("기간선택", new DaySelect());
            panels.Add("카드번호등록/수정", new Card());
            panels.Add("달력", new Moment());
            panels.Values.ToList().ForEach(t => {
                t.Visible = false;
                panel3.Controls.Add(t);
            });
            panels["메인1"].Visible = true;
            sp.panels = panels;
            sp.main = this;
            sp.Show("메인1");
            timer1.Start();
        }

        private void Form1_Load(object sender, EventArgs e) {
            Icon = Icon.FromHandle(Properties.Resources.logo.GetHicon());
        }

        public Label leftLabel { get => left; }

        private void timer1_Tick(object sender, EventArgs e) {
            timeLabel.Text = "현재날짜: " + DateTime.Now.ToString("yyyy-MM-dd(dddd) hh:mm");
        }

        private void left_Click(object sender, EventArgs e) {
            string s = sp.action[sp.action.Count  - 1];
            sp.Show(s, false);
            sp.action.Remove(s);
        }
    }
}
