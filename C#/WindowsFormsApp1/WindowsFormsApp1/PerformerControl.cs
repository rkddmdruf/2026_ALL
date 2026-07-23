using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1 {
    public partial class PerformerControl : UserControl {
        int x = 0, y = 0;
        Entity entity = new Entity();
        public PerformerControl() {
            InitializeComponent();
            entity.Performer.ToList().ForEach(t => {
                tableLayoutPanel1.Controls.Add(GetPanel(t), x, y);
                y += x == 2 ? 1 : 0;
                x += x == 2 ? -x : 1;
            });
        }

        public performerCard GetPanel(Performer p) {
            performerCard panel =  new performerCard() {
                Dock = DockStyle.Fill,
            };
            panel.l1.Text = p.Name.Substring(0, 1);
            panel.l2.Text = p.Name;
            panel.l3.Text = p.Status.ToString().Equals("signed") ? "계약완료" : p.Status.ToString().Equals("tuning") ? "조율중" : "취소";
            panel.l3.ForeColor = p.Status.ToString().Equals("signed") ? Color.Green : p.Status.ToString().Equals("tuning") ? Color.Orange : Color.Red;
            panel.l4.Text = p.Members + "인조 ·" + p.Genre + "·페이 \\ " + p.Fee;
            panel.l5.Text = p.Phone + " " + (p.Email == null ? "" : p.Email);
            return panel;
        }
    }
}
