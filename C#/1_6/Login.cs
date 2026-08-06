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
        string pwString = "";
        public Login() {
            InitializeComponent();
            BackColor = Color.Transparent;

            bool test = false;
            id.KeyPress += (s, e) => {
                if(id.Text.Length >= 13) e.Handled = true;
                if (!char.IsDigit(e.KeyChar) && e.KeyChar != (char)Keys.Back)
                    e.Handled = true;
                if(e.KeyChar == (char)Keys.Back) 
                    test = true;
            };

            id.TextChanged += (s, e) => {
                if ((id.Text.Length == 3 || id.Text.Length == 8) && !test)
                    id.Text += "-";
                test = false;
                id.SelectionStart = id.Text.Length;
            };


            button1.FlatStyle = FlatStyle.Flat;
            button1.FlatAppearance.MouseOverBackColor = button1.BackColor;
            button1.FlatAppearance.MouseDownBackColor = button1.BackColor;
            setNumber();
        }

        private void setNumber() {
            tableLayoutPanel1.RowCount = 4;
            tableLayoutPanel1.ColumnCount = 5;

            tableLayoutPanel1.ColumnStyles.Clear();
            tableLayoutPanel1.RowStyles.Clear();

            for (int i = 0; i < 5; i++) 
                if(i % 2 == 1) tableLayoutPanel1.ColumnStyles.Add(new ColumnStyle(SizeType.Absolute, 10));
                else tableLayoutPanel1.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 33.333f));

            for (int i = 0; i < 4; i++)
                tableLayoutPanel1.RowStyles.Add(new RowStyle(SizeType.Percent, 25f));

            List<int> ints = new List<int>();
            while (!ints.Count.Equals(10)) {
                int n = new Random().Next(0, 10);
                if (!ints.Contains(n)) ints.Add(n);
            }

            foreach (var item in ints) {
                Button button = new Button() {
                    Text = item.ToString(),
                    Dock = DockStyle.Fill,
                    FlatStyle = FlatStyle.Flat,
                    BackColor = Color.White,
                    Margin = new Padding()
                };
                button.Click += (s, e) => { pwString += item.ToString(); pw.Text = pwString; };
                buttons.Add(button);
            }
            var deleteB = new Button() {
                Image = new Bitmap(Properties.Resources.delete, new Size(30, 30)),
                Dock = DockStyle.Fill,
                FlatStyle = FlatStyle.Flat,
                BackColor = Color.White,
                Margin = new Padding()
            };
            var allDeleteB = new Button() {
                Text = "전체\n지움",
                Dock = DockStyle.Fill,
                FlatStyle = FlatStyle.Flat,
                BackColor = Color.White,
                Margin = new Padding()
            };
            deleteB.Click += (s, e) => {
                if (pwString.Length == 0) return;
                pw.Text = pwString = pwString.Substring(0, pwString.Length - 1);
            };
            allDeleteB.Click += (s, e) => {
                pw.Text = pwString = "";
            };

            buttons.Insert(2, deleteB);
            buttons.Insert(9, allDeleteB);

            int number = 0;
            int[] col = { 0, 2, 4 };
            for(int i = 0; i < 4; i++) {
                foreach (int c in col) {
                    tableLayoutPanel1.Controls.Add(buttons[number], c, i);
                    number++;
                }
            }
        }

        private void button1_Click(object sender, EventArgs e) {
            string id = this.id.Text;
            string pw = this.pw.Text;

            if (id.Length == 0 || pw.Length == 0) {
                sp.err("빈칸이 존재합니다. 다시 확인해주세요");
                return;
            }

            user user = sp.entity.user.ToList().Find(u => u.phone.Equals(id) && u.pw.Equals(pw));
            if(user == null) {
                sp.err("회원정보를 확인하시오.");
                return;
            }
            sp.user = user;
            sp.infor(sp.user.uno + "님 환영합니다");
            sp.Show("loginmain");

        }
    }
}
