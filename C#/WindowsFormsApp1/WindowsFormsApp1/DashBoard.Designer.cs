namespace WindowsFormsApp1 {
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
            this.panel1 = new System.Windows.Forms.Panel();
            this.label1 = new System.Windows.Forms.Label();
            this.userChart4 = new WindowsFormsApp1.UserChart();
            this.userChart3 = new WindowsFormsApp1.UserChart();
            this.userChart2 = new WindowsFormsApp1.UserChart();
            this.userChart1 = new WindowsFormsApp1.UserChart();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.panel1.Controls.Add(this.label1);
            this.panel1.Location = new System.Drawing.Point(0, 0);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(820, 50);
            this.panel1.TabIndex = 0;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("맑은 고딕", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(27, 14);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(74, 21);
            this.label1.TabIndex = 0;
            this.label1.Text = "대시보드";
            // 
            // userChart4
            // 
            this.userChart4.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.userChart4.ChartTitle = "";
            this.userChart4.Location = new System.Drawing.Point(428, 362);
            this.userChart4.Name = "userChart4";
            this.userChart4.Size = new System.Drawing.Size(375, 157);
            this.userChart4.TabIndex = 4;
            // 
            // userChart3
            // 
            this.userChart3.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.userChart3.ChartTitle = "";
            this.userChart3.Location = new System.Drawing.Point(18, 362);
            this.userChart3.Name = "userChart3";
            this.userChart3.Size = new System.Drawing.Size(379, 158);
            this.userChart3.TabIndex = 3;
            // 
            // userChart2
            // 
            this.userChart2.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.userChart2.ChartTitle = "";
            this.userChart2.Location = new System.Drawing.Point(428, 188);
            this.userChart2.Name = "userChart2";
            this.userChart2.Size = new System.Drawing.Size(375, 155);
            this.userChart2.TabIndex = 2;
            // 
            // userChart1
            // 
            this.userChart1.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.userChart1.ChartTitle = "";
            this.userChart1.Location = new System.Drawing.Point(18, 188);
            this.userChart1.Name = "userChart1";
            this.userChart1.Size = new System.Drawing.Size(379, 155);
            this.userChart1.TabIndex = 1;
            // 
            // DashBoard
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.Control;
            this.Controls.Add(this.userChart4);
            this.Controls.Add(this.userChart3);
            this.Controls.Add(this.userChart2);
            this.Controls.Add(this.userChart1);
            this.Controls.Add(this.panel1);
            this.Name = "DashBoard";
            this.Size = new System.Drawing.Size(820, 536);
            this.Load += new System.EventHandler(this.DashBoard_Load);
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Label label1;
        private UserChart userChart1;
        private UserChart userChart2;
        private UserChart userChart3;
        private UserChart userChart4;
    }
}
