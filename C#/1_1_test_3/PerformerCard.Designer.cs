namespace _1_1_test_3 {
    partial class PerformerCard {
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
            this.name = new System.Windows.Forms.Label();
            this.status = new System.Windows.Forms.Label();
            this.button1 = new System.Windows.Forms.Button();
            this.name1 = new System.Windows.Forms.Label();
            this.infor1 = new System.Windows.Forms.Label();
            this.infor2 = new System.Windows.Forms.Label();
            this.button2 = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // name
            // 
            this.name.AutoSize = true;
            this.name.Font = new System.Drawing.Font("맑은 고딕", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.name.Location = new System.Drawing.Point(63, 12);
            this.name.Name = "name";
            this.name.Size = new System.Drawing.Size(46, 17);
            this.name.TabIndex = 0;
            this.name.Text = "label1";
            // 
            // status
            // 
            this.status.AutoSize = true;
            this.status.Location = new System.Drawing.Point(64, 33);
            this.status.Name = "status";
            this.status.Size = new System.Drawing.Size(38, 12);
            this.status.TabIndex = 3;
            this.status.Text = "label2";
            // 
            // button1
            // 
            this.button1.BackColor = System.Drawing.Color.White;
            this.button1.Location = new System.Drawing.Point(9, 107);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(69, 23);
            this.button1.TabIndex = 7;
            this.button1.Text = "편집";
            this.button1.UseVisualStyleBackColor = false;
            // 
            // name1
            // 
            this.name1.BackColor = System.Drawing.SystemColors.MenuHighlight;
            this.name1.ForeColor = System.Drawing.Color.White;
            this.name1.Location = new System.Drawing.Point(12, 12);
            this.name1.Name = "name1";
            this.name1.Size = new System.Drawing.Size(46, 43);
            this.name1.TabIndex = 8;
            this.name1.Text = "label2";
            this.name1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // infor1
            // 
            this.infor1.AutoSize = true;
            this.infor1.ForeColor = System.Drawing.Color.Gray;
            this.infor1.Location = new System.Drawing.Point(64, 61);
            this.infor1.Name = "infor1";
            this.infor1.Size = new System.Drawing.Size(38, 12);
            this.infor1.TabIndex = 9;
            this.infor1.Text = "label2";
            // 
            // infor2
            // 
            this.infor2.AutoSize = true;
            this.infor2.ForeColor = System.Drawing.Color.Gray;
            this.infor2.Location = new System.Drawing.Point(64, 82);
            this.infor2.Name = "infor2";
            this.infor2.Size = new System.Drawing.Size(38, 12);
            this.infor2.TabIndex = 10;
            this.infor2.Text = "label2";
            // 
            // button2
            // 
            this.button2.BackColor = System.Drawing.Color.White;
            this.button2.ForeColor = System.Drawing.Color.Red;
            this.button2.Location = new System.Drawing.Point(90, 107);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(69, 23);
            this.button2.TabIndex = 11;
            this.button2.Text = "삭제";
            this.button2.UseVisualStyleBackColor = false;
            // 
            // PerformerCard
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.Controls.Add(this.button2);
            this.Controls.Add(this.infor2);
            this.Controls.Add(this.infor1);
            this.Controls.Add(this.name1);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.status);
            this.Controls.Add(this.name);
            this.Name = "PerformerCard";
            this.Size = new System.Drawing.Size(245, 135);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label name;
        private System.Windows.Forms.Label status;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Label name1;
        private System.Windows.Forms.Label infor1;
        private System.Windows.Forms.Label infor2;
        private System.Windows.Forms.Button button2;
    }
}
