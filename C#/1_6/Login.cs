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
    public partial class Login : UserControl {
        List<Button> buttons = new List<Button>();
        
        public Login() {
            InitializeComponent();
            BackColor = Color.Transparent;

            button1.FlatStyle = FlatStyle.Flat;
            button1.FlatAppearance.MouseOverBackColor = button1.BackColor;
            button1.FlatAppearance.MouseDownBackColor = button1.BackColor;
            setNumber();
        }

        private void setNumber() {
            tableLayoutPanel1.RowCount = 7;
            tableLayoutPanel1.ColumnCount = 3;

            for (int i = 0; i < 3; i++) 
                tableLayoutPanel1.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 33.333f));
            for (int i = 0; i < 4; i++)
                if(i % 2 == 1) tableLayoutPanel1.RowStyles.Add(new RowStyle(SizeType.Absolute, 10));
                else tableLayoutPanel1.RowStyles.Add(new RowStyle(SizeType.Percent, 25f));

            List<int> ints = new List<int>();
            while (!ints.Count.Equals(10)) {
                int n = new Random().Next(0, 10);
                if (!ints.Contains(n)) ints.Add(n);
            }
            foreach (var item in ints)
            {
                Button button = new Button() { 
                    Text = item.ToString(),
                    Dock = DockStyle.Fill,
                    FlatStyle = FlatStyle.Flat,
                    BackColor = Color.White,
                };
            }
        }
    }
}
