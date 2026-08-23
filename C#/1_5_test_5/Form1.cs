using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_5 {
    public partial class Form1 : Form {
        public Form1() {
            InitializeComponent();
            timer1.Start();
            Icon = Properties.Resources.logo;
            titleImage.Image = Properties.Resources.logo.ToBitmap();
        }

        private void Form1_VisibleChanged(object sender, EventArgs e) {
            reload();
        }

        private void reload() {
            button1.Text = (sp.user == null && sp.owner == null) ? "로그인" : "로그아웃";
            adminPanel1.Visible = sp.owner != null;
            userPanel1.Visible = !adminPanel1.Visible;
            nameLabel.Text = sp.user != null ? sp.user.uname : sp.owner != null ? sp.owner.oname : "";

            userPanel1.fp.Controls.Clear();
            userPanel1.fp.Controls.Add(fpLabel());
            userPanel1.fp.SetFlowBreak(userPanel1.fp, true);
            sp.entity.reservation.ToList().Where(t => t.sdate.Value.Date.Equals(DateTime.Now.Date))
                .GroupBy(t => t.hotel.hName)
                .Select(t => new {key = t.Key, value = t.Count()})
                .ToList().ForEach(t => {
                    userPanel1.fp.Controls.Add(fpLabel(t.key + " : " + t.value + "건"));
                    userPanel1.fp.SetFlowBreak(userPanel1.fp, true);
                });

            int i = 1;
            sp.entity.reservation.ToList()
                .Where(t => sp.owner == null || (sp.owner != null && sp.owner.ono.Equals(t.hotel.ono)))
                .GroupBy(t => t.hotel.hName)
                .Select(t => new { key = t.Key, value = t.Count(), n = sp.entity.reservation.ToList().First(c => c.hotel.hName.Equals(t.Key)).hno })
                .OrderByDescending(t => t.value)
                .ThenBy(t => t.n)
                .ToList().Take(3)
                .ToList().ForEach(t => {
                    Controls["imgPanel" + i].BackgroundImage = Properties.Resources.ResourceManager.GetObject("_" + t.n) as Bitmap;
                    Controls["imgPanel" + i].Controls["label" + i].Text = t.key;
                    i++;
                });

            if (sp.owner is null) { return; }
            
            var list = sp.entity.reservation.ToList().Where(t => t.hotel.ono.Equals(sp.owner.ono)).ToList();
            adminPanel1.l1.Text = "·투숙전 : " + list.ToList().Where(t => t.sdate.Value.Date < DateTime.Now.Date).Count() + "건";
            adminPanel1.l2.Text = "·투숙중 : " + list.ToList().Where(t => t.sdate.Value.Date >= DateTime.Now.Date && t.sdate.Value.Date.AddDays(t.day.Value) <= DateTime.Now.Date).Count() + "건";
            adminPanel1.l3.Text = "·투숙후 : " + list.ToList().Where(t => t.sdate.Value.Date.AddDays(t.day.Value) > DateTime.Now.Date).Count() + "건";
        }

        private Label fpLabel(string s = "오늘의 예약현황") {
            return new Label() {
                Text = (s.Equals("오늘의 예약현황") ? "· 오늘의 예약 현황" : " ·" + s),
                Font = titleLabel.Font,
                ForeColor = s.Equals("오늘의 예약현황") ? Color.Red : Color.Black,
                Size = new Size(userPanel1.fp.Width - 30, (int) (Font.Size * 1.4 + 20))
            };
        }

        private void button1_Click(object sender, EventArgs e) {
            if(sp.user is null && sp.owner is null) {
                Hide();
                new Login().ShowDialog();
                Show();
            } else {
                sp.user = null; sp.owner = null;
            }
            reload();
        }

        private void timer1_Tick(object sender, EventArgs e) {
            timeLabel.Text = "오늘 날짜 : " + DateTime.Now.ToString("yyyy-MM-dd");
        }
    }
}
