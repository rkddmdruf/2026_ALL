namespace _1_1_test_3 {
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
            this.label1 = new System.Windows.Forms.Label();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
            this.userChart4 = new _1_1_test_3.userChart();
            this.userChart3 = new _1_1_test_3.userChart();
            this.userChart2 = new _1_1_test_3.userChart();
            this.userChart1 = new _1_1_test_3.userChart();
            this.dashCard1 = new _1_1_test_1.DashCard();
            this.dashCard2 = new _1_1_test_1.DashCard();
            this.dashCard3 = new _1_1_test_1.DashCard();
            this.dashCard4 = new _1_1_test_1.DashCard();
            this.tableLayoutPanel1.SuspendLayout();
            this.tableLayoutPanel2.SuspendLayout();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("맑은 고딕", 12.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(16, 14);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(78, 23);
            this.label1.TabIndex = 0;
            this.label1.Text = "대시보드";
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 2;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Controls.Add(this.userChart4, 1, 1);
            this.tableLayoutPanel1.Controls.Add(this.userChart3, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.userChart2, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.userChart1, 0, 0);
            this.tableLayoutPanel1.Location = new System.Drawing.Point(20, 191);
            this.tableLayoutPanel1.Margin = new System.Windows.Forms.Padding(0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 2;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(862, 394);
            this.tableLayoutPanel1.TabIndex = 1;
            // 
            // tableLayoutPanel2
            // 
            this.tableLayoutPanel2.ColumnCount = 4;
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 25F));
            this.tableLayoutPanel2.Controls.Add(this.dashCard4, 3, 0);
            this.tableLayoutPanel2.Controls.Add(this.dashCard3, 2, 0);
            this.tableLayoutPanel2.Controls.Add(this.dashCard2, 1, 0);
            this.tableLayoutPanel2.Controls.Add(this.dashCard1, 0, 0);
            this.tableLayoutPanel2.Location = new System.Drawing.Point(20, 52);
            this.tableLayoutPanel2.Name = "tableLayoutPanel2";
            this.tableLayoutPanel2.RowCount = 1;
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel2.Size = new System.Drawing.Size(755, 109);
            this.tableLayoutPanel2.TabIndex = 2;
            // 
            // userChart4
            // 
            this.userChart4.Location = new System.Drawing.Point(434, 200);
            this.userChart4.Name = "userChart4";
            this.userChart4.Size = new System.Drawing.Size(425, 191);
            this.userChart4.TabIndex = 3;
            // 
            // userChart3
            // 
            this.userChart3.Location = new System.Drawing.Point(3, 200);
            this.userChart3.Name = "userChart3";
            this.userChart3.Size = new System.Drawing.Size(425, 191);
            this.userChart3.TabIndex = 2;
            // 
            // userChart2
            // 
            this.userChart2.Location = new System.Drawing.Point(434, 3);
            this.userChart2.Name = "userChart2";
            this.userChart2.Size = new System.Drawing.Size(425, 191);
            this.userChart2.TabIndex = 1;
            // 
            // userChart1
            // 
            this.userChart1.Location = new System.Drawing.Point(3, 3);
            this.userChart1.Name = "userChart1";
            this.userChart1.Size = new System.Drawing.Size(425, 191);
            this.userChart1.TabIndex = 0;
            // 
            // dashCard1
            // 
            this.dashCard1.color = System.Drawing.Color.Empty;
            this.dashCard1.Location = new System.Drawing.Point(3, 3);
            this.dashCard1.Name = "dashCard1";
            this.dashCard1.Size = new System.Drawing.Size(182, 103);
            this.dashCard1.TabIndex = 0;
            // 
            // dashCard2
            // 
            this.dashCard2.color = System.Drawing.Color.Empty;
            this.dashCard2.Location = new System.Drawing.Point(191, 3);
            this.dashCard2.Name = "dashCard2";
            this.dashCard2.Size = new System.Drawing.Size(182, 103);
            this.dashCard2.TabIndex = 1;
            // 
            // dashCard3
            // 
            this.dashCard3.color = System.Drawing.Color.Empty;
            this.dashCard3.Location = new System.Drawing.Point(379, 3);
            this.dashCard3.Name = "dashCard3";
            this.dashCard3.Size = new System.Drawing.Size(182, 103);
            this.dashCard3.TabIndex = 2;
            // 
            // dashCard4
            // 
            this.dashCard4.color = System.Drawing.Color.Empty;
            this.dashCard4.Location = new System.Drawing.Point(567, 3);
            this.dashCard4.Name = "dashCard4";
            this.dashCard4.Size = new System.Drawing.Size(182, 103);
            this.dashCard4.TabIndex = 3;
            // 
            // DashBoard
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.tableLayoutPanel2);
            this.Controls.Add(this.tableLayoutPanel1);
            this.Controls.Add(this.label1);
            this.Name = "DashBoard";
            this.Size = new System.Drawing.Size(913, 607);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel2.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
        private userChart userChart4;
        private userChart userChart3;
        private userChart userChart2;
        private userChart userChart1;
        private _1_1_test_1.DashCard dashCard4;
        private _1_1_test_1.DashCard dashCard3;
        private _1_1_test_1.DashCard dashCard2;
        private _1_1_test_1.DashCard dashCard1;
    }
}
