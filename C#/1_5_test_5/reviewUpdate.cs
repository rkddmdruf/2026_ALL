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
    public partial class reviewUpdate : Form {
        int sc = 0;
        review r;
        public reviewUpdate(int reno) {
            r = sp.entity.review.ToList().First(t => t.reno.Equals(reno));
            sc = r.score.Value;
            InitializeComponent();
            Icon = Properties.Resources.logo;
            titleImage.Image = Properties.Resources.logo.ToBitmap();
            textBox1.BackColor = Color.White;
            textBox2.BackColor = Color.White;
            for (int i = 1; i <= 5; i++) {
                int index = i;
                Controls["l" + i].Click += (sender, e) => {
                    if (!r.reservation.uno.Equals(sp.user.uno)) return;
                    sc = index;
                    setStar();
                };
            }
            setStar();
            if (!r.reservation.uno.Equals(sp.user.uno)) {
                Text = "리뷰";
                titleLabel.Text = "리뷰";
                button1.Visible = false;
            }

            textBox1.Text = r.reservation.hotel.hName;
            textBox2.Text = r.reservation.sdate.Value.ToString("yyyy-MM-dd") + "(" + r.reservation.day + ")";
            textBox3.Text = r.review1;
        }

        private void setStar() {
            for (int i = 1; i <= 5; i++) {
                Controls["l" + i].ForeColor = Color.Gray;
            }
            for (int i = 1; i <= sc; i++) {
                Controls["l" + i].ForeColor = Color.Yellow;
            }
            Refresh();
        }

        private void button1_Click(object sender, EventArgs e) {
            r.review1 = textBox3.Text;
            r.score = sc;
            sp.entity.SaveChanges();
        }
    }
}
