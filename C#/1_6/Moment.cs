using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1_6 {
    public partial class Moment : UserControl {
        string[] dayName = "토,월,화,수,목,금,일".Split(',');
        public Moment() {
            InitializeComponent();
            for(int i = 0; i < dayName.Length; i++) {
                dayNameGrid.Controls.Add(new Label {
                    Text = dayName[i],
                    TextAlign = ContentAlignment.MiddleCenter
                });
            }
        }
    }
}
