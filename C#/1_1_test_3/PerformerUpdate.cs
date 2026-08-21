using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1_test_3 {
    public partial class PerformerUpdate : Form {
        Performer p = new Performer();
        public PerformerUpdate(int pno) {
            if (pno != 0) { p = sp.entity.Performer.ToList().First(t => t.Id.Equals(pno)); }
            InitializeComponent();
            Text = pno == 0 ? "새 공연자 등록" : "공연자 편집";
            foreach (var s in "조율중,계약완료,취소".Split(','))
                comboBox1.Items.Add(s);
            if (pno != 0) {
                textBox1.Text = p.Name;
                textBox2.Text = p.Genre;
                textBox3.Text = string.IsNullOrEmpty(p.Phone) ? "" : p.Phone;
                textBox4.Text = string.IsNullOrEmpty(p.Email) ? "" : p.Email;

                numericUpDown1.Value = p.Members;
                numericUpDown2.Value = p.Fee;
                comboBox1.SelectedItem = p.Status.Equals("signed") ? "계약완료" : p.Status.Equals("tuning") ? "조율중" : "취소";
            }

        }

        private void button1_Click(object sender, EventArgs e) {
            Close();
        }

        private void button2_Click(object sender, EventArgs e) {
            if (string.IsNullOrWhiteSpace(textBox1.Text)) {
                sp.err("이름 입력은 필수 입니다.");
                return;
            }
            if (string.IsNullOrWhiteSpace(textBox2.Text)) {
                sp.err("장르 입력은 필수 입니다.");
                return;
            }
            if (!string.IsNullOrEmpty(p.Name) && sp.entity.Performer.ToList().Where(t => t.Name.Equals(textBox1.Text)).Count() != 0) {
                sp.err("중복된 이름입니다.");
                return;
            }

            try {
                var a = textBox3.Text.Split('-');
                if (a.Length != 3) {throw new Exception();}
                if (a[0].Length != 3) { throw new Exception(); }
                if (a[1].Length != 4) { throw new Exception(); }
                if (a[2].Length != 4) { throw new Exception(); }
            } catch (Exception ex) {
                sp.err("연락처는 010-0000-0000 형식입니다.");
                return;
            }
            try {
                var a = textBox4.Text.Split('@');
                var b = a[1].Split('.');
                if (a.Length != 2 || b.Length != 2) { throw new Exception(); }
            } catch (Exception ex) {
                sp.err("이메일 형식을 확인해주세요.");
                return;
            }

            p.Name = textBox1.Text;
            p.Genre = textBox2.Text;
            p.Members = (int) numericUpDown1.Value;
            p.Fee = (int) numericUpDown2.Value;
            p.Phone = textBox3.Text;
            p.Email = string.IsNullOrEmpty(textBox4.Text) ? null : textBox4.Text;
            p.Status = comboBox1.SelectedIndex == 0 ? "tuning" : comboBox1.SelectedIndex == 1 ? "signed" : "cancelled";
            if (string.IsNullOrEmpty(p.Name)) sp.entity.Performer.Add(p);
            sp.entity.SaveChanges();

        }
    }
}
