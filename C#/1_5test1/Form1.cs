using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml.Linq;

namespace _1_5test1 {
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();
            button1.BackColor = Color.White;
        }

        private void Form1_VisibleChanged(object sender, EventArgs e) {
            if (!Visible) return;
            changeStatus();
            showPanel();
            top3();
            setUserPanel();
        }

        private void showPanel() {
            adminPanel1.Visible = sp.owner != null;
            userMain1.Visible = !adminPanel1.Visible;
        }
        private void setUserPanel() {

            FlowLayoutPanel panel = userMain1.flp;
            panel.Controls.Clear();
            panel.Controls.Add(scrollLabel());
            panel.SetFlowBreak(panel, true);

            var list = sp.entity.reservation.ToList().Where(t => t.sdate.Value.Date.Equals(DateTime.Now.Date))
                .GroupBy(t => t.hno)
                .Select(t => new { key = t.Key, value = t.Count() })
                .ToList();
            foreach(var item in list) {
                panel.Controls.Add(scrollLabel(item.key.Value, item.value));
                panel.SetFlowBreak(panel, true);
            }
            setAdminPanel();
        }

        private void setAdminPanel() {
            if(sp.owner is null ) return;
            MessageBox.Show(sp.entity.reservation.ToList().Where(t => t.hotel.ono.Equals(sp.owner.ono)).Count().ToString());
            adminPanel1.label60.Text = "투숙전 : " +
                sp.entity.reservation.ToList().Where(t => t.hotel.ono.Equals(sp.owner.ono) && t.sdate.Value.Date > DateTime.Now.Date).Count() + "건";

            adminPanel1.label70.Text = "투숙중 : " + 
                sp.entity.reservation.ToList().Where(t => t.hotel.ono.Equals(sp.owner.ono) && 
                t.sdate.Value.Date <= DateTime.Now.Date && t.sdate.Value.AddDays(t.day.Value).Date > DateTime.Now.Date).Count() + "건";

            adminPanel1.label80.Text = "투숙후 : " + 
                sp.entity.reservation.ToList().Where(t => t.hotel.ono.Equals(sp.owner.ono) && t.sdate.Value.AddDays(t.day.Value).Date <= DateTime.Now.Date).Count() + "건";
        }

        private Label scrollLabel(int key = 0, int value = 0) {
            hotel h = sp.entity.hotel.ToList().Find(t => t.hno.Equals(key));
            Label l = new Label {
                Text = key == 0 ? "• 오늘의 예약현황" : " •" + h.hName + " : " + value + "건",
                Font = sp.fk(18),
                ForeColor = key == 0 ? Color.Red : Color.Black
            };
            l.Size = new Size(userMain1.flp.Width - 30, l.Height + 5);
            return l;
        }
        private void top3() {
            var list = sp.entity.reservation.ToList()
                .Where(t => sp.owner is null || sp.owner != null && t.hotel.ono.Equals(sp.owner.ono))
                .GroupBy(t => t.hno)
                .Select(t => new { key = t.Key, value = t.Count() })
                .OrderByDescending(t => t.value)
                .ThenByDescending(t => t.key)
                .Take(3);
            int i = 1;
            foreach (var item in list) {
                Controls["imgPanel" + i].BackgroundImage = Properties.Resources.ResourceManager.GetObject("_" + item.key) as Bitmap;
                Controls["imgPanel" + i++].Controls[0].Text = sp.entity.hotel.ToList().Find(t => t.hno.Equals(item.key)).hName;
            }
        }

        private void changeStatus() {
            button1.Text = sp.user is null && sp.owner is null ? "로그인" : "로그아웃";
            timeLabel.Text = DateTime.Now.ToString("오늘 날짜 : yyyy-MM-dd");
            nameLabel.Text = sp.user is null ? (sp.owner is null ? "" : sp.owner.oname) : sp.user.uname;
        }

        private void button1_Click(object sender, EventArgs e) {
            if(sp.user is null && sp.owner is null) {
                Hide();
                new Login().ShowDialog();
                Show();
            } else {
                sp.user = null; sp.owner = null;
                button1.Text = "로그인";
                changeStatus();
                showPanel();
            }
        }
    }
}
