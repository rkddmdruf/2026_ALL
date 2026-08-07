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
        public Panel topPanel;
        public Form1() {
            InitializeComponent();
            sp.main = this;
            topPanel = panel2;
            panel2.Padding = new Padding(5);

            controls.Add("메인1", new NoLoginMain());
            controls.Add("메인2", new LoginMain());
            controls.Add("로그인", new Login());
            controls.Add("기간선택", new DaySelect());
            controls.Add("좌석배치도", new SeatSelect());
            controls.Add("카드번호등록/수정", new CardUpdate());
            controls.Add("달력", new Moment());
            sp.panels = controls;

            List<Seat> testList = new List<Seat>();
            foreach (var item in sp.entity.seat.ToList()) {
                testList.Add(new Seat {
                    No = item.sno,
                    Name = item.sname,
                    Rect = new Rectangle(item.s_x, item.s_y, item.s_w, item.s_h)
                });
            };
            ((SeatSelect)controls["좌석배치도"]).SetMap(Properties.Resources._1, testList, new List<int>(), 1);

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
                sp.Show("메인1");
            } else sp.Show("메인2");
             
            label3.Click += (s, e) => {
                topPanel.Controls.RemoveAt(topPanel.Controls.Count - 1);
                topPanel.Controls.RemoveAt(topPanel.Controls.Count - 1);
                sp.Show(sp.action.Pop(), false);
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
