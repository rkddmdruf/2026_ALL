using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_5 {
    public partial class CustomForm3 : Form {
        public CustomForm3() {
            InitializeComponent();
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e) {

        }

        private void CustomForm3_Load(object sender, EventArgs e) {
            comboBox1.DisplayMember = "aName";   // 화면에 보일 것
            comboBox1.ValueMember = "ano";       // 실제로 쓸 값
            var data = sp.entity.address.ToList();
            data.Insert(0, new address {
                aName = "전체",
                ano = 0,
            });
            comboBox1.DataSource = data;

            sp.entity.hotel.ToList()
                .Where(h => comboBox1.ValueMember.Equals(0) || h.ano.Equals(comboBox1.ValueMember))
                .ToList();
            address a = comboBox1.SelectedItem as address;
        }
    }
}
