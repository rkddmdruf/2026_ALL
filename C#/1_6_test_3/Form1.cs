using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_3 {
    public partial class Form1 : Form {
        Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        public Form1() {
            InitializeComponent();

            panels.Add("메인1", new Main1());
            panels.Add("메인2", new Main2());
            panels.Add("로그인", new Login());
            panels.Add("기간선택", new DaySelect());
            panels.Add("카드등록", new Card());
            panels.Add("달력", new Moment());
            panels.Values.ToList().ForEach(x => panel3.Controls.Add(x));
            sp.panels = panels;
            sp.main = this;
            label2.Text = "현재날짜: " + DateTime.Now.ToString("yyyy-MM-dd(dddd) hh:mm");
            timer1.Start();
            sp.Show("메인1");
        }

        private void timer1_Tick(object sender, EventArgs e) {
            label2.Text = "현재날짜: " + DateTime.Now.ToString("yyyy-MM-dd(dddd) hh:mm");
        }

        private void label3_Click(object sender, EventArgs e) {
            sp.Show(sp.action[sp.action.Count - 1]);
        }

        public Label leftLabel { get => label3; }
    }
}
