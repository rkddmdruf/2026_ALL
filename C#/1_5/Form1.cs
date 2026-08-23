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

namespace _1_5 {
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();

            settingScrollLabel();
            sp.entity.reservation.ToList().Where(t => t.sdate == DateTime.Now.Date)
                .GroupBy(t => t.hotel.hName)
                .Select(t => new { key = t.Key, value = t.Count() })
                .ToList()
                .ForEach(t => settingScrollLabel(t.key, t.value));

            for (int i = 1; i <= 3; i++) {
                Control con = this.Controls["img" + i];
                con.BackColor = Color.White;
                con.Controls["imgLabel" + i].BackColor = Color.Transparent;
                con.Controls["imgLabel" + i].ForeColor = Color.Transparent;
            }
        }

        private void Form1_VisibleChanged(object sender, EventArgs e) {
            if (Visible) {
                showMainPanel();
                statusChange();
                setTop3();
            }
         }

        private Label settingScrollLabel(string key = "", int value = 0) {
            Label l = new Label {
                Text = (key.Length == 0 ? "• 오늘의 예약 현황" : " • " + key + " : " + value + "건"),
                Font = sp.fk(15),
                ForeColor = key.Length == 0 ? Color.Red : Color.Black,
            };
            l.Size = new Size(userMain1.inforPanel.Width - 30, l.Height + 5);
            userMain1.inforPanel.Controls.Add(l);
            userMain1.inforPanel.SetFlowBreak(l, true);
            return l;
        }

        private void showMainPanel() {
            adminMain1.Visible = sp.owner != null;
            userMain1.Visible = !adminMain1.Visible;
        }
        private void setTop3() {
            var top3 = sp.entity.reservation.ToList()
                .Where(t => sp.owner == null || (sp.owner != null && t.hotel.ono.Equals(sp.owner.ono)))
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
                con.Controls["imgLabel" + i++].Text = hname;
            }
        }

        private void login_Click(object sender, EventArgs e) {
            if (sp.user is null && sp.owner is null) {
                Hide();
                new Login().ShowDialog();
                Show();
            } else {
                sp.user = null;
                sp.owner = null;
                setTop3();
                showMainPanel();
                statusChange();
            }
        }

        private void statusChange() {
            login.Text = sp.user is null && sp.owner is null ? "로그인" : "로그아웃";
            label2.Visible = sp.user != null || sp.owner != null;
            label2.Text = sp.user?.uname ?? sp.owner?.oname ?? "";
            label1.Text = DateTime.Now.ToString("오늘 날짜 : yyyy-MM-dd");
            if (sp.owner is null) return;
        }
    }
}
