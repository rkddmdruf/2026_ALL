using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_1 {
    public partial class Store : UserControl {
        public Store() {
            InitializeComponent();
            reload();
        }

        private void reload() {
            dataGridView1.Columns.Clear();
            dataGridView1.DataSource = null;

            dataGridView1.DataSource = sp.entity.Vendor.ToList().Where(t => t.Name.ToLower().Contains(textBox1.Text.ToLower()) || t.Kind.ToLower().Contains(textBox1.Text.ToLower()))
                .Select(t => new {
                    번호 = t.Id,
                    업체명 = t.Name,
                    업종 = t.Kind,
                    대표 = t.Owner,
                    연락처 = t.Phone,
                    임대료 = "\\ " + t.Rent.ToString("N0")
                ,
                    배치부스 = boothName(t.Id),
                    상태 = t.Status.Equals("in") ? "입점완료" : t.Status.Equals("wait") ? "대기" : "철수"
                })
                .ToList();
            dataGridView1.Columns[0].Visible = false;
            DataGridViewButtonColumn b1 = new DataGridViewButtonColumn() {
                Name = "편집",
                HeaderText = "",
                Text = "편집",
                UseColumnTextForButtonValue = true
            };
            DataGridViewButtonColumn b2 = new DataGridViewButtonColumn() {
                Name = "삭제",
                HeaderText = "",
                Text = "삭제",
                UseColumnTextForButtonValue = true
            };
            dataGridView1.Columns.Add(b1);
            dataGridView1.Columns.Add(b2);
        }
        private string boothName(int id) {
            var a = sp.entity.Booth.ToList().Where(c => c.VendorId.Equals(id)).ToList().FirstOrDefault();
            if (a == null) return "미배치";
            return a.Code + " " + a.Name;
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e) {
            if(e.RowIndex < 0) return;
            if (dataGridView1.Columns[e.ColumnIndex].Name.Equals("편집")) {
                int? id = dataGridView1.Rows[e.RowIndex].Cells["번호"].Value as int?;
                if(id != null) {
                    new StoreUpdate(id.Value).ShowDialog();
                    reload();
                }
            }
            if (dataGridView1.Columns[e.ColumnIndex].Name.Equals("삭제")) {
                int? id = dataGridView1.Rows[e.RowIndex].Cells["번호"].Value as int?;
                if (id != null) {
                    if(sp.check(dataGridView1.Rows[e.RowIndex].Cells["업체명"].Value + "을(를) 삭제할까요?") == DialogResult.Yes) {
                        sp.entity.Vendor.Remove(sp.entity.Vendor.ToList().FirstOrDefault(t => t.Id.Equals(id)));
                        sp.entity.SaveChanges();
                        reload();
                    }
                }
            }
        }

        private void Store_VisibleChanged(object sender, EventArgs e) {
            reload();
        }

        private void textBox1_TextChanged(object sender, EventArgs e) {
            reload();
        }

        private void button1_Click(object sender, EventArgs e) {
            new StoreUpdate(0).ShowDialog();
            sp.infor("계속");
            reload();
        }
    }
}
