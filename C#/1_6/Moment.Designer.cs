namespace _1_6 {
    partial class Moment {
        /// <summary> 
        /// 필수 디자이너 변수입니다.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// 사용 중인 모든 리소스를 정리합니다.
        /// </summary>
        /// <param name="disposing">관리되는 리소스를 삭제해야 하면 true이고, 그렇지 않으면 false입니다.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region 구성 요소 디자이너에서 생성한 코드

        /// <summary> 
        /// 디자이너 지원에 필요한 메서드입니다. 
        /// 이 메서드의 내용을 코드 편집기로 수정하지 마세요.
        /// </summary>
        private void InitializeComponent() {
            this.panel1 = new System.Windows.Forms.Panel();
            this.panel3 = new System.Windows.Forms.Panel();
            this.nextLabel = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.selectDateLabel = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.dayNamePanel = new System.Windows.Forms.Panel();
            this.dayNameGrid = new System.Windows.Forms.TableLayoutPanel();
            this.panel2 = new System.Windows.Forms.Panel();
            this.momentLabel = new System.Windows.Forms.Label();
            this.right = new System.Windows.Forms.Label();
            this.left = new System.Windows.Forms.Label();
            this.comboBox1 = new System.Windows.Forms.ComboBox();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.panel1.SuspendLayout();
            this.panel3.SuspendLayout();
            this.dayNamePanel.SuspendLayout();
            this.panel2.SuspendLayout();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.Color.White;
            this.panel1.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.panel1.Controls.Add(this.tableLayoutPanel1);
            this.panel1.Controls.Add(this.panel3);
            this.panel1.Controls.Add(this.dayNamePanel);
            this.panel1.Controls.Add(this.panel2);
            this.panel1.Location = new System.Drawing.Point(9, 16);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(399, 314);
            this.panel1.TabIndex = 0;
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.comboBox1);
            this.panel3.Controls.Add(this.nextLabel);
            this.panel3.Controls.Add(this.label2);
            this.panel3.Controls.Add(this.selectDateLabel);
            this.panel3.Controls.Add(this.label1);
            this.panel3.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panel3.Location = new System.Drawing.Point(0, 281);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(397, 31);
            this.panel3.TabIndex = 3;
            // 
            // nextLabel
            // 
            this.nextLabel.AutoSize = true;
            this.nextLabel.Location = new System.Drawing.Point(353, 10);
            this.nextLabel.Name = "nextLabel";
            this.nextLabel.Size = new System.Drawing.Size(29, 12);
            this.nextLabel.TabIndex = 3;
            this.nextLabel.Text = "다음";
            this.nextLabel.Click += new System.EventHandler(this.nextLabel_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(160, 10);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(53, 12);
            this.label2.TabIndex = 2;
            this.label2.Text = "입장시간";
            // 
            // selectDateLabel
            // 
            this.selectDateLabel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.selectDateLabel.Location = new System.Drawing.Point(43, 5);
            this.selectDateLabel.Name = "selectDateLabel";
            this.selectDateLabel.Size = new System.Drawing.Size(100, 23);
            this.selectDateLabel.TabIndex = 1;
            this.selectDateLabel.Text = "label2";
            this.selectDateLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(8, 10);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(29, 12);
            this.label1.TabIndex = 0;
            this.label1.Text = "날짜";
            // 
            // dayNamePanel
            // 
            this.dayNamePanel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.dayNamePanel.Controls.Add(this.dayNameGrid);
            this.dayNamePanel.Dock = System.Windows.Forms.DockStyle.Top;
            this.dayNamePanel.Location = new System.Drawing.Point(0, 38);
            this.dayNamePanel.Name = "dayNamePanel";
            this.dayNamePanel.Size = new System.Drawing.Size(397, 38);
            this.dayNamePanel.TabIndex = 2;
            // 
            // dayNameGrid
            // 
            this.dayNameGrid.ColumnCount = 7;
            this.dayNameGrid.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 14.28572F));
            this.dayNameGrid.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 14.28572F));
            this.dayNameGrid.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 14.28572F));
            this.dayNameGrid.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 14.28572F));
            this.dayNameGrid.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 14.28572F));
            this.dayNameGrid.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 14.28572F));
            this.dayNameGrid.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 14.28572F));
            this.dayNameGrid.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dayNameGrid.Location = new System.Drawing.Point(0, 0);
            this.dayNameGrid.Name = "dayNameGrid";
            this.dayNameGrid.RowCount = 1;
            this.dayNameGrid.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.dayNameGrid.Size = new System.Drawing.Size(395, 36);
            this.dayNameGrid.TabIndex = 1;
            // 
            // panel2
            // 
            this.panel2.Controls.Add(this.momentLabel);
            this.panel2.Controls.Add(this.right);
            this.panel2.Controls.Add(this.left);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel2.Location = new System.Drawing.Point(0, 0);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(397, 38);
            this.panel2.TabIndex = 0;
            // 
            // momentLabel
            // 
            this.momentLabel.AutoSize = true;
            this.momentLabel.Font = new System.Drawing.Font("맑은 고딕", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.momentLabel.Location = new System.Drawing.Point(176, 10);
            this.momentLabel.Name = "momentLabel";
            this.momentLabel.Size = new System.Drawing.Size(39, 15);
            this.momentLabel.TabIndex = 2;
            this.momentLabel.Text = "label3";
            // 
            // right
            // 
            this.right.Location = new System.Drawing.Point(278, 10);
            this.right.Name = "right";
            this.right.Size = new System.Drawing.Size(17, 18);
            this.right.TabIndex = 1;
            this.right.Text = "▶";
            this.right.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // left
            // 
            this.left.Enabled = false;
            this.left.Location = new System.Drawing.Point(96, 9);
            this.left.Name = "left";
            this.left.Size = new System.Drawing.Size(17, 18);
            this.left.TabIndex = 0;
            this.left.Text = "◀";
            this.left.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // comboBox1
            // 
            this.comboBox1.FormattingEnabled = true;
            this.comboBox1.Location = new System.Drawing.Point(223, 5);
            this.comboBox1.Name = "comboBox1";
            this.comboBox1.Size = new System.Drawing.Size(109, 20);
            this.comboBox1.TabIndex = 4;
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 2;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 76);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 2;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(397, 205);
            this.tableLayoutPanel1.TabIndex = 4;
            // 
            // Moment
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.panel1);
            this.Name = "Moment";
            this.Size = new System.Drawing.Size(416, 330);
            this.panel1.ResumeLayout(false);
            this.panel3.ResumeLayout(false);
            this.panel3.PerformLayout();
            this.dayNamePanel.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.panel2.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Label left;
        private System.Windows.Forms.Label momentLabel;
        private System.Windows.Forms.Label right;
        private System.Windows.Forms.Panel dayNamePanel;
        private System.Windows.Forms.TableLayoutPanel dayNameGrid;
        private System.Windows.Forms.Panel panel3;
        private System.Windows.Forms.Label nextLabel;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label selectDateLabel;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ComboBox comboBox1;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    }
}
