using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6 {
    public partial class Form1 : Form {


        public Form1() {
            InitializeComponent();
            this.Icon = Icon.FromHandle(Properties.Resources.logo.GetHicon());
            this.Text = "메인";
            label2.BackColor = Color.Transparent;
            mainPanel.Controls.Add(new Login());

            timeLabel.BackColor = Color.Transparent;
            timer1.Start();

            mainPanel.BackColor = Color.Transparent;

        }

        private void timer1_Tick(object sender, EventArgs e) {
            timeLabel.Text = DateTime.Now.ToString("현재날짜: yyyy-MM-dd(dddd) HH:mm");
        }

        private void Form1_Load(object sender, EventArgs e) {
            timeLabel.Text = DateTime.Now.ToString("현재날짜: yyyy-MM-dd(dddd) HH:mm");
        }
    }
}
