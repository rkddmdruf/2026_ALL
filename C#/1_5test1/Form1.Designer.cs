namespace _1_5test1 {
    partial class Form1 {
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

        #region Windows Form 디자이너에서 생성한 코드

        /// <summary>
        /// 디자이너 지원에 필요한 메서드입니다. 
        /// 이 메서드의 내용을 코드 편집기로 수정하지 마세요.
        /// </summary>
        private void InitializeComponent() {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.label1 = new System.Windows.Forms.Label();
            this.panel1 = new System.Windows.Forms.Panel();
            this.panel2 = new System.Windows.Forms.Panel();
            this.timeLabel = new System.Windows.Forms.Label();
            this.nameLabel = new System.Windows.Forms.Label();
            this.button1 = new System.Windows.Forms.Button();
            this.imgPanel1 = new System.Windows.Forms.Panel();
            this.imgLabel1 = new System.Windows.Forms.Label();
            this.imgPanel2 = new System.Windows.Forms.Panel();
            this.imgLabel2 = new System.Windows.Forms.Label();
            this.imgPanel3 = new System.Windows.Forms.Panel();
            this.imgLabel3 = new System.Windows.Forms.Label();
            this.panel3 = new System.Windows.Forms.Panel();
            this.userMain1 = new _1_5test1.userMain();
            this.adminPanel1 = new _1_5test1.adminPanel();
            this.topPanel1 = new _1_5test1.topPanel();
            this.panel1.SuspendLayout();
            this.panel2.SuspendLayout();
            this.imgPanel1.SuspendLayout();
            this.imgPanel2.SuspendLayout();
            this.imgPanel3.SuspendLayout();
            this.panel3.SuspendLayout();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("맑은 고딕", 18F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(238, 66);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(271, 32);
            this.label1.TabIndex = 1;
            this.label1.Text = "Seoul Booking System";
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.Color.Black;
            this.panel1.Controls.Add(this.panel2);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panel1.Location = new System.Drawing.Point(0, 508);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(759, 53);
            this.panel1.TabIndex = 2;
            // 
            // panel2
            // 
            this.panel2.BackColor = System.Drawing.Color.White;
            this.panel2.Controls.Add(this.nameLabel);
            this.panel2.Controls.Add(this.timeLabel);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panel2.Location = new System.Drawing.Point(0, 3);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(759, 50);
            this.panel2.TabIndex = 0;
            // 
            // timeLabel
            // 
            this.timeLabel.AutoSize = true;
            this.timeLabel.Font = new System.Drawing.Font("맑은 고딕", 15.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.timeLabel.Location = new System.Drawing.Point(12, 9);
            this.timeLabel.Name = "timeLabel";
            this.timeLabel.Size = new System.Drawing.Size(238, 30);
            this.timeLabel.TabIndex = 3;
            this.timeLabel.Text = "Seoul Booking System";
            // 
            // nameLabel
            // 
            this.nameLabel.AutoSize = true;
            this.nameLabel.Font = new System.Drawing.Font("맑은 고딕", 18F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.nameLabel.Location = new System.Drawing.Point(628, 9);
            this.nameLabel.Name = "nameLabel";
            this.nameLabel.Size = new System.Drawing.Size(86, 32);
            this.nameLabel.TabIndex = 4;
            this.nameLabel.Text = "강응결";
            // 
            // button1
            // 
            this.button1.Font = new System.Drawing.Font("맑은 고딕", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.button1.Location = new System.Drawing.Point(636, 12);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(111, 30);
            this.button1.TabIndex = 3;
            this.button1.Text = "로그인";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // imgPanel1
            // 
            this.imgPanel1.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.imgPanel1.Controls.Add(this.imgLabel1);
            this.imgPanel1.Location = new System.Drawing.Point(76, 395);
            this.imgPanel1.Name = "imgPanel1";
            this.imgPanel1.Size = new System.Drawing.Size(172, 98);
            this.imgPanel1.TabIndex = 4;
            // 
            // imgLabel1
            // 
            this.imgLabel1.AutoSize = true;
            this.imgLabel1.BackColor = System.Drawing.Color.Transparent;
            this.imgLabel1.ForeColor = System.Drawing.Color.White;
            this.imgLabel1.Location = new System.Drawing.Point(3, 84);
            this.imgLabel1.Name = "imgLabel1";
            this.imgLabel1.Size = new System.Drawing.Size(38, 12);
            this.imgLabel1.TabIndex = 0;
            this.imgLabel1.Text = "label2";
            // 
            // imgPanel2
            // 
            this.imgPanel2.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.imgPanel2.Controls.Add(this.imgLabel2);
            this.imgPanel2.Location = new System.Drawing.Point(303, 395);
            this.imgPanel2.Name = "imgPanel2";
            this.imgPanel2.Size = new System.Drawing.Size(172, 98);
            this.imgPanel2.TabIndex = 5;
            // 
            // imgLabel2
            // 
            this.imgLabel2.AutoSize = true;
            this.imgLabel2.BackColor = System.Drawing.Color.Transparent;
            this.imgLabel2.ForeColor = System.Drawing.Color.White;
            this.imgLabel2.Location = new System.Drawing.Point(3, 84);
            this.imgLabel2.Name = "imgLabel2";
            this.imgLabel2.Size = new System.Drawing.Size(38, 12);
            this.imgLabel2.TabIndex = 0;
            this.imgLabel2.Text = "label2";
            // 
            // imgPanel3
            // 
            this.imgPanel3.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.imgPanel3.Controls.Add(this.imgLabel3);
            this.imgPanel3.Location = new System.Drawing.Point(520, 395);
            this.imgPanel3.Name = "imgPanel3";
            this.imgPanel3.Size = new System.Drawing.Size(172, 98);
            this.imgPanel3.TabIndex = 6;
            // 
            // imgLabel3
            // 
            this.imgLabel3.AutoSize = true;
            this.imgLabel3.BackColor = System.Drawing.Color.Transparent;
            this.imgLabel3.ForeColor = System.Drawing.Color.White;
            this.imgLabel3.Location = new System.Drawing.Point(3, 84);
            this.imgLabel3.Name = "imgLabel3";
            this.imgLabel3.Size = new System.Drawing.Size(38, 12);
            this.imgLabel3.TabIndex = 0;
            this.imgLabel3.Text = "label2";
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.userMain1);
            this.panel3.Controls.Add(this.adminPanel1);
            this.panel3.Location = new System.Drawing.Point(0, 100);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(759, 290);
            this.panel3.TabIndex = 7;
            // 
            // userMain1
            // 
            this.userMain1.BackColor = System.Drawing.Color.White;
            this.userMain1.Location = new System.Drawing.Point(0, 0);
            this.userMain1.Name = "userMain1";
            this.userMain1.Size = new System.Drawing.Size(759, 290);
            this.userMain1.TabIndex = 2;
            // 
            // adminPanel1
            // 
            this.adminPanel1.Location = new System.Drawing.Point(0, 0);
            this.adminPanel1.Name = "adminPanel1";
            this.adminPanel1.Size = new System.Drawing.Size(759, 290);
            this.adminPanel1.TabIndex = 1;
            // 
            // topPanel1
            // 
            this.topPanel1.BackColor = System.Drawing.Color.Black;
            this.topPanel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.topPanel1.Location = new System.Drawing.Point(0, 0);
            this.topPanel1.Name = "topPanel1";
            this.topPanel1.Size = new System.Drawing.Size(759, 53);
            this.topPanel1.TabIndex = 0;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(759, 561);
            this.Controls.Add(this.panel3);
            this.Controls.Add(this.imgPanel3);
            this.Controls.Add(this.imgPanel2);
            this.Controls.Add(this.imgPanel1);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.topPanel1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Form1";
            this.Text = "메인";
            this.VisibleChanged += new System.EventHandler(this.Form1_VisibleChanged);
            this.panel1.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.panel2.PerformLayout();
            this.imgPanel1.ResumeLayout(false);
            this.imgPanel1.PerformLayout();
            this.imgPanel2.ResumeLayout(false);
            this.imgPanel2.PerformLayout();
            this.imgPanel3.ResumeLayout(false);
            this.imgPanel3.PerformLayout();
            this.panel3.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private topPanel topPanel1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Label nameLabel;
        private System.Windows.Forms.Label timeLabel;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Panel imgPanel1;
        private System.Windows.Forms.Label imgLabel1;
        private System.Windows.Forms.Panel imgPanel2;
        private System.Windows.Forms.Label imgLabel2;
        private System.Windows.Forms.Panel imgPanel3;
        private System.Windows.Forms.Label imgLabel3;
        private System.Windows.Forms.Panel panel3;
        private adminPanel adminPanel1;
        private userMain userMain1;
    }
}

