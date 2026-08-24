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
    public partial class StoreUpdate : Form
    {
        Vendor v = new Vendor();
        public StoreUpdate(int? s)
        {
            Text = s == null && s == 0 ? "새 업체 등록" : "업체 편집";
            InitializeComponent();
            foreach(var str in "푸드,체험,스폰서,굿즈,기타".Split(',')) comboBox1.Items.Add(str);
            foreach (var str in "대기,입점완료,철수".Split(',')) comboBox2.Items.Add(str);
            if (s != null && s != 0)
            {
                v = sp.entity.Vendor.ToList().Where(t => t.Id.Equals(s.Value)).ToList()[0];
                var a = sp.entity.Vendor.ToList().First(t => t.Id.Equals(s));
                textBox1.Text = a.Name;
                comboBox1.SelectedItem = a.Kind;
                comboBox2.SelectedItem = a.Status.Equals("in") ? "대기" : "입점완료";
                numericUpDown1.Value = a.Rent;
                textBox2.Text = a.Owner;
                textBox3.Text = a.Phone;

            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (textBox1.Text.Length <= 0)
            {
                sp.err("업체명은 필수입니다.");
                return;
            }
            if(sp.entity.Vendor.ToList().Where(t => t.Name.Equals(textBox1.Text)).Count() != 0)
            {
                sp.err("중복된 업체명입니다.");
                return;
            }
            try
            {
                string[] str = textBox1.Text.Split('-');
                if(str.Length != 3) throw new Exception();
                if (!str[0].Equals("010") || str[1].Length != 4 || str[2].Length != 4) throw new Exception();
            }
            catch (Exception ex)
            {
                sp.err("연락처는 010-0000-0000 형식입니다.");
                return;
            }

            v.Name = textBox1.Text;
            v.Kind = comboBox1.SelectedItem.ToString();
            v.Status = comboBox2.SelectedItem.ToString();
            v.Owner = textBox2.Text;
            v.Phone = textBox3.Text;
            v.Rent = int.Parse(numericUpDown1.Value.ToString());
            sp.entity.Vendor.Add(v);
            sp.entity.SaveChanges();
            Close();
        }
    }
}
