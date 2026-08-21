using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5_test_3 {
    public partial class Form1 : Form {
        userPanel up = new userPanel();
        adminPanel ap = new adminPanel();
        public Form1() {
            InitializeComponent();
            
            panel3.Controls.Add(up);
            panel3.Controls.Add(ap);
            setFlowPanel();
        }

        private void Form1_VisibleChanged(object sender, EventArgs e) {
            if (!Visible) return;
            reload();
        }

        private void reload() {
            ap.Visible = sp.owner != null;
            up.Visible = !ap.Visible;

            button1.Text = sp.user == null && sp.owner == null ? "로그인" : "로그아웃";
            timeLabel.Text = "오늘 날짜: " + DateTime.Now.ToString("yyyy-MM-dd");
            nameLabel.Text = sp.user == null ? sp.owner == null ? "" : sp.owner.oname : sp.user.uname;

            int i = 1;
            sp.entity.reservation.ToList()
                .Where(t => sp.owner == null || (sp.owner != null && t.hotel.ono.Equals(sp.owner.ono)))
                .GroupBy(t => t.hotel.hno)
                .Select(t => new { key = t.Key, value = t.Count() }).OrderByDescending(t => t.value).ThenByDescending(t => t.key).Take(3).ToList()
                .ForEach(t => {
                    var h = sp.entity.hotel.ToList().First(c => c.hno.Equals(t.key));
                    var p = Controls["pic" + i];
                    p.BackgroundImage = Properties.Resources.ResourceManager.GetObject("_" + h.hno) as Image;
                    p.Controls["label" + i].Text = h.hName;
                    p.Controls["label" + i].ForeColor = Color.White;
                    i++;
                });
        }
        private void setFlowPanel() {
            up.fp.Controls.Clear();
            up.fp.Controls.Add(new Label {
                Text = "• 오늘의 예약현황",
                Font = sp.fk(18),
                ForeColor = Color.Red,
                Size = new Size(up.fp.Width - 30,(int) (sp.fk(18).Size * 1.4) + 10)
            });
            up.fp.SetFlowBreak(up.fp, true);
            sp.entity.reservation.ToList().Where(t => t.sdate.Value.Date.Equals(DateTime.Now.Date)).GroupBy(t => t.hotel.hName).Select(t => new { key = t.Key, value = t.Count() })
                .ToList().ForEach(t => {
                    up.fp.Controls.Add(new Label {
                        Text = " • " + t.key + " : " + t.value + "건",
                        Font = sp.fk(18),
                        Size = new Size(up.fp.Width - 30, (int)(sp.fk(18).Size * 1.4) + 10)
                    });
                    up.fp.SetFlowBreak(up.fp, true);
                });

            
        }

        private void button1_Click(object sender, EventArgs e) {
            if (sp.user == null && sp.owner == null) {
                Hide();
                new Login().ShowDialog();
                Show();
                return;
            }
            sp.user = null; sp.owner = null;
            reload();
        }
    }
}
