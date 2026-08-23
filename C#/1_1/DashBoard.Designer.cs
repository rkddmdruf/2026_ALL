namespace _1_1 {
    partial class DashBoard {
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
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
            this.label1 = new System.Windows.Forms.Label();
            this.userChart3 = new WindowsFormsApp1.UserChart();
            this.userChart1 = new WindowsFormsApp1.UserChart();
            this.userChart2 = new WindowsFormsApp1.UserChart();
            this.userChart4 = new WindowsFormsApp1.UserChart();
            this.tableLayoutPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.tableLayoutPanel1.ColumnCount = 3;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 15F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Controls.Add(this.userChart3, 0, 2);
            this.tableLayoutPanel1.Controls.Add(this.userChart1, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.userChart2, 2, 0);
            this.tableLayoutPanel1.Controls.Add(this.userChart4, 2, 2);
            this.tableLayoutPanel1.Location = new System.Drawing.Point(14, 169);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 3;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 15F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(876, 446);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // tableLayoutPanel2
            // 
            this.tableLayoutPanel2.ColumnCount = 4;
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.Location = new System.Drawing.Point(14, 50);
            this.tableLayoutPanel2.Name = "tableLayoutPanel2";
            this.tableLayoutPanel2.RowCount = 1;
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel2.Size = new System.Drawing.Size(759, 100);
            this.tableLayoutPanel2.TabIndex = 1;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("맑은 고딕", 11.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(10, 17);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(69, 20);
            this.label1.TabIndex = 2;
            this.label1.Text = "대시보드";
            // 
            // userChart3
            // 
            this.userChart3.ChartTitle = "";
            this.userChart3.Dock = System.Windows.Forms.DockStyle.Fill;
            this.userChart3.Location = new System.Drawing.Point(3, 233);
            this.userChart3.Name = "userChart3";
            this.userChart3.Size = new System.Drawing.Size(424, 210);
            this.userChart3.TabIndex = 2;
            // 
            // userChart1
            // 
            this.userChart1.ChartTitle = "";
            this.userChart1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.userChart1.Location = new System.Drawing.Point(3, 3);
            this.userChart1.Name = "userChart1";
            this.userChart1.Size = new System.Drawing.Size(424, 209);
            this.userChart1.TabIndex = 0;
            // 
            // userChart2
            // 
            this.userChart2.ChartTitle = "";
            this.userChart2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.userChart2.Location = new System.Drawing.Point(448, 3);
            this.userChart2.Name = "userChart2";
            this.userChart2.Size = new System.Drawing.Size(425, 209);
            this.userChart2.TabIndex = 1;
            // 
            // userChart4
            // 
            this.userChart4.ChartTitle = "";
            this.userChart4.Dock = System.Windows.Forms.DockStyle.Fill;
            this.userChart4.Location = new System.Drawing.Point(448, 233);
            this.userChart4.Name = "userChart4";
            this.userChart4.Size = new System.Drawing.Size(425, 210);
            this.userChart4.TabIndex = 3;
            // 
            // DashBoard
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.label1);
            this.Controls.Add(this.tableLayoutPanel2);
            this.Controls.Add(this.tableLayoutPanel1);
            this.Name = "DashBoard";
            this.RightToLeft = System.Windows.Forms.RightToLeft.No;
            this.Size = new System.Drawing.Size(909, 632);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
        private System.Windows.Forms.Label label1;
        private WindowsFormsApp1.UserChart userChart1;
        private WindowsFormsApp1.UserChart userChart2;
        private WindowsFormsApp1.UserChart userChart4;
        private WindowsFormsApp1.UserChart userChart3;
    }
}
