using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_1 {
    public partial class PerformerForm : UserControl {
        public PerformerForm() {
            InitializeComponent();
            reload();
        }

        public void reload() {
            tableLayoutPanel1.Controls.Clear();
            tableLayoutPanel1.RowStyles.Clear();

            sp.entity.Performer.ToList()
                .Where(t => t.Name.ToLower().Contains(textBox1.Text.ToLower()) || t.Genre.ToLower().Contains(textBox1.Text.ToLower())).ToList()
                .ForEach(item => {
                PerformerCard p = new PerformerCard() {
                    Size = new Size(237, 150),
                    Dock = DockStyle.Fill,
                };

                p.nameLabel.Text = item.Name;
                p.statusLabel.Text = item.Status.Equals("signed") ? "계약완료" : item.Status.Equals("tuning") ? "조율중" : "취소";
                p.statusLabel.ForeColor = item.Status.Equals("signed") ? Color.ForestGreen : item.Status.Equals("tuning") ? Color.Chocolate : Color.Black;
                p.label1.Text = item.Members + "인조·" + item.Genre + "·페이 \\ " + item.Fee.ToString("N0");
                p.label2.Text = item.Phone + " " + (item.Email is null ? "" : item.Email);
                p.b1.Click += (s, e) => {
                    new PerformerUpdate(item.Id).ShowDialog();
                };
                p.b2.Click += (s, e) => {
                    if (sp.check(item.Name + "을(를) 삭제할까요?") != DialogResult.Yes) return;
                    sp.entity.Performer.Remove(item);
                    sp.entity.SaveChanges();
                    reload();
                };

                tableLayoutPanel1.Controls.Add(p, 0, 0);
            });
        }

        private void textBox1_TextChanged(object sender, EventArgs e) {
            reload();
        }

        private void button1_Click(object sender, EventArgs e) {
            new PerformerUpdate(0).ShowDialog();
        }
    }
}
