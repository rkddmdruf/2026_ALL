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
    public partial class StoreUpdate : Form {
        Vendor v;
        int sno = 0;
        public StoreUpdate(int sno) {
            this.sno = sno;
            v = sp.entity.Vendor.ToList().FirstOrDefault(t => t.Id.Equals(sno));
            if(v == null) { v = new Vendor(); }
            InitializeComponent();

            foreach (var s in new string[] { "푸드", "체험", "스폰서", "굿즈", "기타" })
                comboBox1.Items.Add(s);
            foreach(var s in new string[] {"대기", "입점완료", "철수"})
                comboBox2.Items.Add(s);
            if (sno != 0) {
                textBox1.Text = v.Name;
                textBox2.Text = v.Owner;
                textBox3.Text = v.Phone;

                numericUpDown1.Value = v.Rent;
                comboBox1.SelectedItem = v.Kind;
                comboBox2.SelectedIndex = v.Status.Equals("in") ? 1 : v.Status.Equals("wait") ? 0 : 2;
            }
        }

        private void button1_Click(object sender, EventArgs e) {
            Close();
        }

        private void button2_Click(object sender, EventArgs e) {
            if (string.IsNullOrEmpty(textBox1.Text)) {
                sp.err("업체명은 필수입니다.");
                return;
            }
            if (sno == 0 && sp.entity.Vendor.ToList().Where(t => t.Name.Equals(textBox1.Text)).Count() != 0) {
                sp.err("중복된 업체명입니다.");
                return;
            }
            try {
                string[] s = textBox3.Text.Split('-');
                if (s.Length != 3) new Exception();
                int[] ss = { 3, 4, 4 };
                for (int i = 0; i < s.Length; i++) {
                    if (s[i].Length != ss[i]) new Exception();
                }
            } catch (Exception ex) {
                sp.err("연락처는 010-0000-0000 형식입니다.");
                return;
            }

            v.Name = textBox1.Text;
            v.Owner = textBox2.Text;
            v.Phone = textBox3.Text;

            v.Kind = comboBox1.SelectedItem.ToString();
            v.Status = comboBox2.SelectedIndex == 0 ? "wait" : comboBox2.SelectedIndex == 1 ? "in" : "out";
            v.Rent = (int)numericUpDown1.Value;

            if (sno == 0) {
                sp.entity.Vendor.Add(v);
            }
            sp.entity.SaveChanges();
        }
    }
}
