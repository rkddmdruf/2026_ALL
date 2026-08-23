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
    public partial class ReviewForm : Form {
        bool myReview = true;
        public ReviewForm() {
            InitializeComponent();
            Icon = Properties.Resources.logo;
            titleImage.Image = Properties.Resources.logo.ToBitmap();
        }

        private void button1_Click(object sender, EventArgs e) {
            myReview = true;
            reload();
        }

        private void button2_Click(object sender, EventArgs e) {
            myReview = false;
            reload();
        }

        private void reload() {
            dataGridView1.DataSource = null;
            dataGridView1.DataSource = sp.entity.review.ToList()
                .Where(t => (myReview && t.reservation.uno.Equals(sp.user.uno)) || (!myReview && !t.reservation.uno.Equals(sp.user.uno)))
                .Select(t => new { 호텔명 = t.reservation.hotel.hName, 별점 = t.score, 내용 = t.review1, 번호 = t.reno }).ToList();
            dataGridView1.Columns[0].Width = 200;
            dataGridView1.Columns[1].Width = 70;
            dataGridView1.Columns[1].DefaultCellStyle.Alignment = DataGridViewContentAlignment.MiddleCenter;
            dataGridView1.Columns[3].Visible = false;
        }

        private void dataGridView1_CellContentDoubleClick(object sender, DataGridViewCellEventArgs e) {
            if(e.RowIndex < 0) return;
            this.Hide();
            new reviewUpdate(int.Parse(dataGridView1.Rows[e.RowIndex].Cells[3].Value.ToString())).ShowDialog();
            this.Show();
            reload();
        }

        private void ReviewForm_Load(object sender, EventArgs e) {
            reload();
        }
    }
}
