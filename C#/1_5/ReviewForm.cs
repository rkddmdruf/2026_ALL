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
    public partial class ReviewForm : Form {
        public ReviewForm() {
            InitializeComponent();
        }

        private void ReviewForm_Load(object sender, EventArgs e) {
            reData(true);
        }
        private void button1_Click(object sender, EventArgs e) {
            reData(true);
        }

        private void button2_Click(object sender, EventArgs e) {
            reData(false);
        }
        private void reData(bool myData) {
            dataGridView1.DataSource = sp.entity.review.ToList().Where(t => {
                if (myData) return t.reservation.uno == sp.user.uno;
                return t.reservation.uno != sp.user.uno;
            }).Select(t => new { 호텔명 = t.reservation.hotel.hName, 별점 = t.score, 내용 = t.review1, 번호 = t.reno }).ToList();

            dataGridView1.Columns[0].Width = 200;
            dataGridView1.Columns[1].Width = 70;
            dataGridView1.Columns[1].DefaultCellStyle.Alignment = DataGridViewContentAlignment.MiddleCenter;
            dataGridView1.Columns[3].Visible = false;
        }

        private void dataGridView1_CellDoubleClick(object sender, DataGridViewCellEventArgs e) {
            if(e.RowIndex < 0) return;
            Hide();
            new ReviewUpdate((int)dataGridView1.Rows[e.RowIndex].Cells[3].Value).ShowDialog();
            Show();
        }
    }
}
