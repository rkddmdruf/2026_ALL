using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement.TextBox;

namespace _1_1
{
    public partial class Tiket : UserControl
    {
        List<int> ints = new List<int> { 2, 4, 3, 1 };
        public Tiket()
        {
            InitializeComponent();
            foreach (var a in "카드,현금,계좌이체".Split(',')) comboBox2.Items.Add(a);
            sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList().ForEach(t => comboBox1.Items.Add(t.Name + "—\\ " + t.Price.ToString("N0")));
            comboBox1.SelectedIndex = 0; comboBox2.SelectedIndex = 0;
            reload();
        }

        private void reload()
        {
            dataGridView1.DataSource = null;
            dataGridView1.Rows.Clear();
            dataGridView1.Columns.Clear();
            dataGridView1.DataSource = sp.entity.Sale.ToList().OrderBy(t => DateTime.Compare(t.SoldAt, DateTime.Now)).ToList().Take(20).Select(t => new { 번호 = "T-" + t.Id.ToString("D6"), 시각 = t.SaleTime, 구매자 = t.Buyer, 종류 = t.TicketType.Name, 
                수량 = t.Qty, 합계 = "\\" + (t.TicketType.Price * t.Qty).ToString("N0"), 결제 = t.Pay, 상태 = status(t.Status)}).ToList();
            setCards();
        }
        private void setCards()
        {
            tableLayoutPanel1.Controls.Clear();
            
            sp.entity.TicketType.ToList().OrderBy(t => ints.IndexOf(t.Id)).ToList().ForEach(t =>
            {
                TicketCard tc = new TicketCard(t.Sold, t.Capacity)
                {
                    Dock = DockStyle.Fill,
                };
                tc.nameLabel.Text = t.Name;
                tc.priceLabel.Text = "\\ " + t.Price.ToString("N0");
                tc.ticketLabel1.Text = t.Capacity - t.Sold <= 0 ? "매진" : "잔여 " + (t.Capacity - t.Sold).ToString();
                tc.ticketLabel1.ForeColor = t.Capacity - t.Sold < 20 ? Color.Orange : Color.ForestGreen;
                tc.ticketLabel2.Text = "판매 " + t.Sold + " / " + t.Capacity;
                tableLayoutPanel1.Controls.Add(tc);
            });
        }
        private string status(string s)
        {
            return s.Equals("ok") ? "완료" : "환불";
        }
        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            setPriceLabel();
        }

        private void setPriceLabel()
        {
            priceLabel.Text = "\\ " + (sp.entity.TicketType.ToList().FirstOrDefault(t => t.Id.Equals(ints[comboBox1.SelectedIndex])).Price * (int)numericUpDown1.Value).ToString("N0");
        }

        private void numericUpDown1_ValueChanged(object sender, EventArgs e)
        {
            setPriceLabel();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (textBox1.Text.Length == 0)
            {
                sp.err("구매자 이름 필수");
                return;
            }

            if(textBox2.Text.Length != 0)
            {
                try
                {
                    string[] str = textBox1.Text.Split('-');
                    if (str.Length != 3) throw new Exception();
                    if (!str[0].Equals("010") || str[1].Length != 4 || str[2].Length != 4) throw new Exception();
                }
                catch (Exception ex)
                {
                    sp.err("연락처는 010-0000-0000 형식입니다.");
                    return;
                }
            }

            TicketType ti = sp.entity.TicketType.ToList().FirstOrDefault(t => t.Id.Equals(ints[comboBox1.SelectedIndex]));
            int total = ti.Capacity - ti.Sold;
            if (total < numericUpDown1.Value)
            {

                sp.err(ti.Name + " 잔여 " + total + "매뿐 — 요청 " + ((int)numericUpDown1.Value - total) +  "매 초과.");
                return;
            }
            ti.Sold = ti.Sold + (int) numericUpDown1.Value;
            sp.entity.SaveChanges();

            Sale s = new Sale();
            s.FestivalId = 1;
            s.SaleTime = "14:33";
            s.Buyer = textBox1.Text;
            s.Phone = textBox2.Text.Length == 0 ? null : textBox2.Text;
            s.TicketTypeId = ints[comboBox1.SelectedIndex];
            s.Qty = (int) numericUpDown1.Value;
            s.Memo = textBox3.Text.Length == 0 ? null : textBox3.Text;
            s.Status = "ok";
            s.SoldAt = DateTime.Now;
            s.Pay = comboBox2.SelectedItem.ToString();

            sp.entity.Sale.Add(s);
            sp.entity.SaveChanges();

            sp.infor("발권 완료");
            reload();
        }
    }
}
