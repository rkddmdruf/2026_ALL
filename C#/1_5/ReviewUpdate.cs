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
    public partial class ReviewUpdate : Form {
        review re;
        int star = 3;

        public ReviewUpdate(int reno) {
            re = sp.entity.review.Find(reno);
            star = re.score.Value;

            InitializeComponent();
            if(re.reservation.uno != sp.user.uno) {
                Text = "리뷰";
                for(int i = 0; i < 3; i++) {
                    ((TextBox) Controls["textBox" + (i + 1)]).ReadOnly = true;
                    Controls["textBox" + (i + 1)].BackColor = Color.White;
                }
                button1.Visible = false;
            }
            textBox1.Text = re.reservation.hotel.hName;
            textBox2.Text = re.reservation.sdate.Value.ToString("yyyy-MM-dd") + "(" + re.reservation.day + "박)";
            textBox3.Text = re.review1.ToString();
            
        }

        private void label4_Paint(object s, PaintEventArgs e) {
            Label l = (Label) s;
            string all = new string('★', 5);
            string on = new string('★', star);

            TextRenderer.DrawText(e.Graphics, all, l.Font, new Point(0, 0), Color.Gray,
                TextFormatFlags.NoPadding);
            TextRenderer.DrawText(e.Graphics, on, l.Font, new Point(0, 0), Color.Gold,
                TextFormatFlags.NoPadding);
        }

        private void button1_Click(object sender, EventArgs e) {
            if (textBox3.Text.Length == 0) {
                sp.err("리뷰를 입력하세요.");
                return;
            }
            try {
                re.review1 = textBox3.Text;
                sp.entity.SaveChanges();
                sp.infor("리뷰가 수정되었습니다.");
                Close();
            } catch (Exception ex) {
                sp.err("글씨가 넘쳤다");
            }
        }
    }
}
