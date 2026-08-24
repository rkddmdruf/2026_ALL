using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1
{
    public partial class Store : UserControl
    {
        public Store()
        {
            InitializeComponent();
            setting();

        }

        private void setting()
        {
            dataGridView1.DataSource = null;
            dataGridView1.Rows.Clear();
            dataGridView1.Columns.Clear();

            dataGridView1.DataSource = sp.entity.Vendor.ToList()
                .Where(t => t.Name.Contains(textBox1.Text) || t.Kind.Contains(textBox1.Text))
                .Select(t => new
                {
                    번호 = t.Id,
                    업체명 = t.Name,
                    업종 = t.Kind,
                    대표 = t.Owner,
                    연락처 = t.Phone,
                    임대료 = "\\ " + t.Rent.ToString("N0"),
                    배치부스 = getString(t.Id),
                    상태 = t.Status.Equals("in") ? "입점완료" : "대기"
                }).ToList();
            dataGridView1.Columns[0].Visible = false;

            DataGridViewButtonColumn edit = new DataGridViewButtonColumn
            {
                Name = "편집",
                HeaderText = "",
                Text = "편집",
                UseColumnTextForButtonValue = true
            };

            DataGridViewButtonColumn delete = new DataGridViewButtonColumn
            {
                Name = "삭제",
                HeaderText = "",
                Text = "삭제",
                UseColumnTextForButtonValue = true
            };

            dataGridView1.Columns.Add(edit);
            dataGridView1.Columns.Add(delete);
        }

        private string getString(int id)
        {
            var a = sp.entity.Booth.ToList().FirstOrDefault(t => t.VendorId != null && t.VendorId.Equals(id));
            if (a is null) return "미배치";
            return a.Code + a.Name;
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0)
                return;
            if (dataGridView1.Columns[e.ColumnIndex].Name.Equals("삭제"))
            {
                int id = (int)dataGridView1.Rows[e.RowIndex].Cells["번호"].Value;

                var vendor = sp.entity.Vendor.First(t => t.Id == id);

                sp.entity.Vendor.Remove(vendor);
                sp.entity.SaveChanges();

                setting();
            }
            if (dataGridView1.Columns[e.ColumnIndex].Name.Equals("편집"))
            {
                int id = (int)dataGridView1.Rows[e.RowIndex].Cells["번호"].Value;
                new StoreUpdate(id).ShowDialog();
                setting();
            }
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            setting();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            new StoreUpdate(null).ShowDialog();
        }
    }
}
