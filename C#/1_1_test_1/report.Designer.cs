namespace _1_1_test_1 {
    partial class report {
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
            this.label2 = new System.Windows.Forms.Label();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.userChart1 = new _1_1_test_1.userChart();
            this.userChart2 = new _1_1_test_1.userChart();
            this.tableLayoutPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("맑은 고딕", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label2.ForeColor = System.Drawing.Color.Black;
            this.label2.Location = new System.Drawing.Point(17, 15);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(109, 21);
            this.label2.TabIndex = 1;
            this.label2.Text = "통계 / 리포트";
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 3;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 20F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Controls.Add(this.userChart2, 2, 0);
            this.tableLayoutPanel1.Controls.Add(this.userChart1, 0, 0);
            this.tableLayoutPanel1.Location = new System.Drawing.Point(21, 50);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 1;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 546F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(844, 546);
            this.tableLayoutPanel1.TabIndex = 2;
            // 
            // userChart1
            // 
            this.userChart1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.userChart1.Location = new System.Drawing.Point(3, 3);
            this.userChart1.Name = "userChart1";
            this.userChart1.Size = new System.Drawing.Size(406, 540);
            this.userChart1.TabIndex = 0;
            // 
            // userChart2
            // 
            this.userChart2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.userChart2.Location = new System.Drawing.Point(435, 3);
            this.userChart2.Name = "userChart2";
            this.userChart2.Size = new System.Drawing.Size(406, 540);
            this.userChart2.TabIndex = 2;
            // 
            // report
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.tableLayoutPanel1);
            this.Controls.Add(this.label2);
            this.Name = "report";
            this.Size = new System.Drawing.Size(885, 610);
            this.VisibleChanged += new System.EventHandler(this.report_VisibleChanged);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private userChart userChart1;
        private userChart userChart2;
    }
}
