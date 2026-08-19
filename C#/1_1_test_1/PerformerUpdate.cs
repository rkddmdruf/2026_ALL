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
    public partial class PerformerUpdate : Form {
        Performer p = new Performer();
        int pno = 0;
        public PerformerUpdate(int pno) {
            this.pno = pno;
            if (pno != 0) { p = sp.entity.Performer.ToList().FirstOrDefault(t => t.Id.Equals(pno)); }
            InitializeComponent();
            foreach(var s in "조율중,계약완료,취소".Split(','))
                comboBox1.Items.Add(s);
            comboBox1.SelectedIndex = 0;
            if (pno != 0) {
                textBox1.Text = p.Name;
                textBox2.Text = p.Genre.ToString();
                textBox3.Text = p.Phone.ToString();
                textBox4.Text = p.Email == null ? "" : p.Email;
                comboBox1.SelectedIndex = p.Status.Equals("signed") ? 1 : p.Status.Equals("tuning") ? 0 : 2;
                numericUpDown1.Value = p.Members;
                numericUpDown2.Value = p.Fee;
            }
        }

        private void button1_Click(object sender, EventArgs e) {
            Close();
        }

        private void button2_Click(object sender, EventArgs e) {
            if(textBox1.Text.Length == 0) {
                sp.err("이름은 필수 입니다.");
                return;
            }
            if (sp.entity.Performer.ToList().Where(t => t.Name.Equals(p.Name)).Count() != 0) {
                sp.err("중복된 이름입니다.");
                return;
            }

            try {
                string[] s = textBox3.Text.Split('-');
                if(s.Length != 3) new Exception();
                int[] ss = { 3, 4, 4 };
                for (int i = 0; i < s.Length; i++) {
                    if (s[i].Length != ss[i]) new Exception();
                }
            }catch(Exception ex) {
                sp.err("연락처는 010-0000-0000 형식입니다.");
                return;
            }
            try {
                string[] s = textBox3.Text.Split('@');
                if (s.Length != 2) new Exception();
                if (s[1].Split('.').Length != 2) new Exception();
            } catch (Exception ex) {
                sp.err("이메일 형식을 확인해주세요.");
                return;
            }

            p.Name = textBox1.Text;
            p.Genre = textBox2.Text;
            p.Phone = textBox3.Text;
            p.Email = textBox4.Text;
            p.Members = (int)numericUpDown1.Value;
            p.Fee = (int)numericUpDown2.Value;
            p.Status = comboBox1.SelectedIndex == 0 ? "조율중" : comboBox1.SelectedIndex == 1 ? "계약완료" : "취소";

            if(pno == 0) {
                sp.entity.Performer.Add(p);
            }
            sp.entity.SaveChanges();
            Close();
        }
    }
}
