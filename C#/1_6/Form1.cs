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

        public  Dictionary<string, UserControl> controls = new Dictionary<string, UserControl>();
        public Label leftLabel;

        public Form1() {
            InitializeComponent();
            sp.main = this;

            controls.Add("nologinmain", new NoLoginMain());
            controls.Add("loginmain", new LoginMain());
            controls.Add("login", new Login());
            controls.Add("dayselect", new DaySelect());
            controls.Add("seatselect", new SeatSelect());
            controls.Add("cardupdate", new CardUpdate());
            controls.Add("moment", new Moment());
            sp.panels = controls;


            this.Icon = Icon.FromHandle(Properties.Resources.logo.GetHicon());
            this.Text = "메인";

            label2.BackColor = Color.Transparent;
            label3.BackColor = Color.Transparent;
            timeLabel.BackColor = Color.Transparent;
            mainPanel.BackColor = Color.Transparent;

            leftLabel = label3;

            /*SeatSelect seatMap = new SeatSelect();
            mainPanel.Controls.Add(seatMap);

            List<Seat> testList = new List<Seat>() ;
            foreach (var item in sp.entity.seat.ToList())
            {
                testList.Add(new Seat {
                    No = item.sno,
                    Name = item.sname,
                    Rect = new Rectangle(item.s_x, item.s_y, item.s_w, item.s_h)
                });
            };
            seatMap.SetMap(Properties.Resources._1, testList, new List<int>(), 1);*/

            
            foreach (var value in controls.Values)
                mainPanel.Controls.Add(value);
            if (sp.user == null) {
                sp.Show("nologinmain");
            } else sp.Show("loginmain");
            sp.Show("moment");
             
            label3.Click += (s, e) => {
                sp.Show(sp.action.Pop());
            };


            timer1.Start();
        }

        private void timer1_Tick(object sender, EventArgs e) {
            timeLabel.Text = DateTime.Now.ToString("현재날짜: yyyy-MM-dd(dddd) HH:mm");
        }

        private void Form1_Load(object sender, EventArgs e) {
            timeLabel.Text = DateTime.Now.ToString("현재날짜: yyyy-MM-dd(dddd) HH:mm");
        }
    }
}
