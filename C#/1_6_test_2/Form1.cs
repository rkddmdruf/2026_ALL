using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6_test_2 {
    public partial class Form1 : Form {
        Dictionary<string, UserControl> panels = new Dictionary<string, UserControl>();
        public Form1() {
            InitializeComponent();
            timer1.Start();
            Icon = Icon.FromHandle(Properties.Resources.logo.GetHicon());

            panels.Add("메인1", new Main1());
            panels.Add("메인2", new Main2());
            panels.Add("기간선택", new DaySelect());
            panels.Add("로그인", new Login());
            sp.panels = panels;

            sp.Show("메인2");
            panels.Values.ToList().ForEach(p => panel3.Controls.Add(p));
        }

        private void timer1_Tick(object sender, EventArgs e) {
            label3.Text = "현재날짜: " + DateTime.Now.ToString("yyyy-MM-dd(dddd) hh:mm");
        }

        private void left_Click(object sender, EventArgs e) {
            sp.Show(sp.action[sp.action.Count - 1]);
        }
    }
}
