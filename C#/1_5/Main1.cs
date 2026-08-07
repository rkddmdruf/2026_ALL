using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {
    public partial class Main1 : UserControl {
        public Main1() {
            InitializeComponent();
            if(DesignMode) { return; }
        }

        private Label settingScrollLabel(string key = "", int value = 0) {
            Label l = new Label {
                Text = (value == 0 ? "• 오늘의 예약 현황" : " • " + key + " : " + value + "건"),
                Font = sp.fk(15),
                ForeColor = value == 0 ? Color.Red : Color.Black,
            };
            l.Size = new Size(reservationData.Width - 30, l.Height + 5);
            reservationData.Controls.Add(l);
            reservationData.SetFlowBreak(l, true);
            return l;
        }

        private void Main1_VisibleChanged(object sender, EventArgs e) {
            var now = DateTime.Now.Date;
            settingScrollLabel();
            sp.entity.reservation.Where(t => t.sdate == now).GroupBy(t => t.hotel.hName).Select(t => new { key = t.Key, value = t.Count() }).ToList().ForEach(t => {
                settingScrollLabel(t.key, t.value);
            });

            var top3 = sp.entity.reservation
                .GroupBy(t => t.hno)
                .Select(t => new { key = t.Key, value = t.Count() })
                .OrderByDescending(t => t.value)
                .ThenByDescending(x => x.key)
                .Take(3);
            int i = 1;
            foreach (var item in top3) {
                string hname = sp.entity.hotel.Where(t => t.hno == item.key).First().hName;
                Control con = this.Controls["img" + i];
                con.BackgroundImage = Properties.Resources.ResourceManager.GetObject("_" + item.key) as Image;
                con.Controls["imgLabel" + i].BackColor = Color.Transparent;
                con.Controls["imgLabel" + i].ForeColor = Color.White;
                con.Controls["imgLabel" + i++].Text = hname;
            }
        }
    }
}
