using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_5 {
    public partial class PerformerForm : UserControl {
        public PerformerForm() {
            InitializeComponent();
            reload();
        }

        private void reload() {
            tableLayoutPanel2.Controls.Clear();
            tableLayoutPanel2.RowCount = 0;
            tableLayoutPanel2.RowStyles.Clear();

            sp.entity.Performer.ToList()
                .Where(t => t.Name.ToLower().Contains(textBox1.Text.ToLower()) || t.Genre.ToLower().Contains(textBox1.Text.ToLower()))
                .ToList()
                .ForEach(t => {
                    PerformerCard c = new PerformerCard();
                    c.inforLabel1.Text = t.Members + "인조·" + t.Genre + "·페이 \\ " + t.Fee.ToString("N0");
                    c.inforLabel2.Text = (t.Phone == null ? "" : t.Phone) + " " + (t.Email == null ? "" : t.Email);
                    c.name1Label.Text = t.Name.Substring(0, 1);
                    c.nameLabel.Text = t.Name;
                    c.statusLabel.Text = t.Status.Equals("signed") ? "계약완료" : t.Status.Equals("tuning") ? "조율중" : "취소";
                    c.statusLabel.ForeColor = t.Status.Equals("signed") ? Color.ForestGreen : t.Status.Equals("tuning") ? Color.Chocolate : Color.Red;

                    c.b1.Click += (sender, e) => {
                        new PerformerUpdate(t.Id).ShowDialog();
                        reload();
                    };
                    c.b2.Click += (sender, e) => {
                        if(sp.check(t.Name + "을(를) 삭제할까요?") == DialogResult.Yes) { 
                            sp.entity.Performer.Remove(t);
                            sp.entity.SaveChanges();
                            reload();
                        }
                    };
                    tableLayoutPanel2.Controls.Add(c);
            });
        }

        private void textBox1_TextChanged(object sender, EventArgs e) {
            reload();
        }

        private void button1_Click(object sender, EventArgs e) {
            new PerformerUpdate(0).ShowDialog();
            reload();
        }
    }
}
