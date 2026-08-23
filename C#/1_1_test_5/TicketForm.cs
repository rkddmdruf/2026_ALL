using _1_1_test_5;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_6 {
    public partial class TicketForm : UserControl {
        List<TicketType> ticketTypes;
        public TicketForm() {
            InitializeComponent();
            reload();
        }
        private void reload() {
            dataGridView1.DataSource = null;
            dataGridView1.DataSource = sp.entity.Sale.ToList().OrderBy(t => (t.SoldAt.Date - DateTime.Now.Date).Days).Take(20)
                .Select(t => new {번호 = "T-" + t.Id.ToString("D6"), 시각 = t.SaleTime, 구매자 = t.Buyer, 종류 = t.TicketType.Name, 수량 = t.Qty, 
                    합계 = "\\ " + (t.Qty + t.TicketType.Price).ToString("N0"), 결제 = t.Pay, 상태 = t.Status.Equals("ok") ? "완료" : t.Status.Equals("refund") ? "환불" : "??"}) .ToList();
        }

        private void button1_Click(object sender, EventArgs e) {

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e) {
            setPriceText();
        }

        private void numericUpDown1_ValueChanged(object sender, EventArgs e) {
            setPriceText();
        }

        private void setPriceText() {
            price.Text = numericUpDown1.Value.ToString("N0");
        }
    }
}
