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
    public partial class TicketForm : UserControl {
        List<int> ints = new List<int> { 2, 4, 3, 1 };
        public TicketForm() {
            InitializeComponent();

            sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList().ForEach(t => comboBox1.Items.Add(t.Name + "— \\ " + t.Price.ToString("N0")));
            foreach (var s in "현금,카드,계좌 이체".Split(',')) comboBox2.Items.Add(s);
            comboBox1.SelectedIndex = 0;
            comboBox2.SelectedIndex = 0;
            reload();
        }

        private void reload() {
            dataGridView1.DataSource = null;
            sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList()
                .ForEach(t => {
                    ticketCard ti = (ticketCard)tableLayoutPanel1.Controls["ticketCard" + (ints.IndexOf(t.Id) + 1)];
                    ti.nameLabel.Text = t.Name;
                    int count = (t.Capacity - t.Sold);
                    ti.countLabel.Text = count <= 0 ? "매진" : "잔여 " + count;
                    ti.countLabel.ForeColor = count <= 20 ? Color.Chocolate : Color.ForestGreen;
                    ti.priceLabel.Text = "\\ " + t.Price.ToString("N0");
                    ti.countAll.Text = "판매 " + t.Sold + " / " + t.Capacity;
                    ti.value = t.Sold; ti.max = t.Capacity;
                    ti.Refresh();
                });

            dataGridView1.DataSource = sp.entity.Sale.ToList().Take(20)
                .Select(t => new { 번호 = "T-" + t.Id.ToString("D6"), 시각 = t.SaleTime, 구매자 = t.Buyer, 종류 = t.TicketType.Name, 수량 = t.Qty, 
                    합계 =  "\\ " + (t.Qty * t.TicketType.Price).ToString("N0"), 결제 = t.Pay, 상태 = t.Status.Equals("ok") ? "완료" : "환불"
                })
                .ToList();
        }

        private void button1_Click(object sender, EventArgs e) {
            if (string.IsNullOrEmpty(textBox1.Text)) {
                sp.err("구매자 이름 필수");
                return;
            }
            try {
                string[] str = textBox3.Text.Split('-');
                if (str.Length != 3) new Exception();
                int[] ss = { 3, 4, 4 };
                for (int i = 0; i < str.Length; i++) {
                    if (str[i].Length != ss[i]) new Exception();
                }
            } catch (Exception ex) {
                sp.err("연락처는 010-0000-0000 형식입니다.");
                return;
            }
            TicketType ti = sp.entity.TicketType.ToList().FirstOrDefault(t => t.Id.Equals(ints[comboBox1.SelectedIndex]));
            int total = ti.Capacity - ti.Sold;
            if (total < numericUpDown1.Value) {
                sp.err(ti.Name + " 잔여 " + total + "매뿐 — 요청 " + ((int)numericUpDown1.Value - total) + "매 초과.");
                return;
            }
            ti.Sold = ti.Sold + (int)numericUpDown1.Value;
            sp.entity.SaveChanges();

            Sale s = new Sale();
            s.FestivalId = 1;
            s.SaleTime = "14:33";
            s.Buyer = textBox1.Text;
            s.Phone = textBox2.Text.Length == 0 ? null : textBox2.Text;
            s.TicketTypeId = ints[comboBox1.SelectedIndex];
            s.Qty = (int)numericUpDown1.Value;
            s.Memo = textBox3.Text.Length == 0 ? null : textBox3.Text;
            s.Status = "ok";
            s.SoldAt = DateTime.Now;
            s.Pay = comboBox2.SelectedItem.ToString();

            sp.entity.Sale.Add(s);
            sp.entity.SaveChanges();

            sp.infor("발권 완료");
            reload();
        }

        private void numericUpDown1_ValueChanged(object sender, EventArgs e) {
            priceLabel.Text = "\\ " + (((int)numericUpDown1.Value) * sp.entity.TicketType.ToList().FirstOrDefault(t => t.Id.Equals(ints[comboBox1.SelectedIndex])).Price).ToString("N0");
        }

        private void TicketForm_VisibleChanged(object sender, EventArgs e) {
            priceLabel.Text = "\\ " + (((int)numericUpDown1.Value) * sp.entity.TicketType.ToList().FirstOrDefault(t => t.Id.Equals(ints[comboBox1.SelectedIndex])).Price).ToString("N0");
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e) {
            priceLabel.Text = "\\ " + (((int)numericUpDown1.Value) * sp.entity.TicketType.ToList().FirstOrDefault(t => t.Id.Equals(ints[comboBox1.SelectedIndex])).Price).ToString("N0");
        }
    }
}
