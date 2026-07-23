using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1 {
    public partial class PerformUpdate : Form {
        int? per;
        public PerformUpdate(int? per) {
            InitializeComponent();
            this.per = per;
            u1.lb.Text = "이름 *";
            u2.lb.Text = "장르";
            u3.lb.Text = "연락처";
            u4.lb.Text = "이메일";

            n1.Minimum = 1;
            n1.ThousandsSeparator = true;
            n1.Maximum = decimal.MaxValue;
            n1.Value = 1;
            n1.Increment = 1;

            n2.Minimum = 0;
            n2.ThousandsSeparator = true;
            n2.Maximum = decimal.MaxValue;
            n2.Value = 0;
            n2.Increment = 1;
        }

        private void button2_Click(object sender, EventArgs e) {
            Entity entity = new Entity();
            Performer pe = entity.Performer.ToList().Find(t => t.Id.Equals(per));
            if (u1.tb.Text.Length == 0) { getter.err("이름은 필수입니다."); return; }
            if (entity.Performer.Where(t => t.Name.Equals(u1.tb.Name)).ToList().Count != 0) { getter.err("중복된 이름입니다."); return; }
            entity.SaveChanges();
        }

        private void button1_Click(object sender, EventArgs e) {
            Dispose();
        }
    }
}
