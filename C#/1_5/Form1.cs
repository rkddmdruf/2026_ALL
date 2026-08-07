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
            for(int i = 1; i <= 3; i++) {
                Control con = this.Controls["img" + i];
                con.BackColor = Color.White;
                con.Controls["imgLabel" + i].BackColor = Color.Transparent;
                con.Controls["imgLabel" + i].ForeColor = Color.Transparent;
            }
        }

        private void Form1_VisibleChanged(object sender, EventArgs e) {
            if (Visible) {
                statusChange();
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
                    con.Controls["imgLabel" + i++].Text = hname;
                }
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
                statusChange();
            }
        }

        private void statusChange() {
            login.Text = sp.user is null ? "로그인" : "로그아웃";
            label2.Visible = sp.user != null;
            label2.Text = sp.user?.uname ?? "";
            label1.Text = DateTime.Now.ToString("오늘 날짜 : yyyy-MM-dd");
        }
    }
}
