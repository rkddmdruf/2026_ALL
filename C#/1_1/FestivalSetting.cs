using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_1 {
    public partial class FestivalSetting : UserControl {
        Entity entity = new Entity();
        Festival f;
        public FestivalSetting() {
            f = entity.Festival.ToList()[0];
            InitializeComponent();
        }

        private void FestivalSetting_VisibleChanged(object sender, EventArgs e) {
            textBox1.Text = f.Name;
            textBox2.Text = f.Place;
            dateTimePicker1.Format = DateTimePickerFormat.Custom;
            dateTimePicker1.CustomFormat = "yyyy-MM-dd";
            dateTimePicker2.Format = DateTimePickerFormat.Custom;
            dateTimePicker2.CustomFormat = "yyyy-MM-dd";

            dateTimePicker1.Value = f.StartDate.Date;
            dateTimePicker2.Value = f.EndDate.Date;
        }

        private void button1_Click(object sender, EventArgs e) {
            if (textBox1.Text.Length <= 0) { sp.err("축제명은 필수입니다."); return; }
            if (dateTimePicker1.Value > dateTimePicker2.Value) {
                sp.err("종료일이 시작일보다 앞섭니다.");
                return;
            }
            f.Name = textBox1.Text;
            f.StartDate = dateTimePicker1.Value;
            f.EndDate = dateTimePicker2.Value;
            f.Place = textBox2.Text;
            f.UpdatedAt = DateTime.Now;
            entity.SaveChanges();
            sp.infor("축제 정보가 저장되었습니다.");
        }
    }
}
