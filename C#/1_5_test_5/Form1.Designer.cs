namespace _1_5_test_5 {
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
            this.components = new System.ComponentModel.Container();
            this.panel1 = new System.Windows.Forms.Panel();
            this.titleImage = new System.Windows.Forms.PictureBox();
            this.titleLabel = new System.Windows.Forms.Label();
            this.panel2 = new System.Windows.Forms.Panel();
            this.timeLabel = new System.Windows.Forms.Label();
            this.subTitleLabel = new System.Windows.Forms.Label();
            this.panel3 = new System.Windows.Forms.Panel();
            this.userPanel1 = new _1_5_test_5.userPanel();
            this.button1 = new System.Windows.Forms.Button();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.nameLabel = new System.Windows.Forms.Label();
            this.imgPanel1 = new System.Windows.Forms.Panel();
            this.imgPanel2 = new System.Windows.Forms.Panel();
            this.imgPanel3 = new System.Windows.Forms.Panel();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.adminPanel1 = new _1_5_test_5.adminPanel();
            ((System.ComponentModel.ISupportInitialize)(this.titleImage)).BeginInit();
            this.panel3.SuspendLayout();
            this.imgPanel1.SuspendLayout();
            this.imgPanel2.SuspendLayout();
            this.imgPanel3.SuspendLayout();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.Color.Black;
            this.panel1.Location = new System.Drawing.Point(0, 54);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(1500, 3);
            this.panel1.TabIndex = 0;
            // 
            // titleImage
            // 
            this.titleImage.Location = new System.Drawing.Point(12, 3);
            this.titleImage.Name = "titleImage";
            this.titleImage.Size = new System.Drawing.Size(50, 50);
            this.titleImage.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            this.titleImage.TabIndex = 1;
            this.titleImage.TabStop = false;
            // 
            // titleLabel
            // 
            this.titleLabel.AutoSize = true;
            this.titleLabel.Font = new System.Drawing.Font("맑은 고딕", 18F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.titleLabel.Location = new System.Drawing.Point(78, 9);
            this.titleLabel.Name = "titleLabel";
            this.titleLabel.Size = new System.Drawing.Size(62, 32);
            this.titleLabel.TabIndex = 2;
            this.titleLabel.Text = "메인";
            // 
            // panel2
            // 
            this.panel2.BackColor = System.Drawing.Color.Black;
            this.panel2.Location = new System.Drawing.Point(0, 525);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(1500, 3);
            this.panel2.TabIndex = 4;
            // 
            // timeLabel
            // 
            this.timeLabel.AutoSize = true;
            this.timeLabel.Font = new System.Drawing.Font("맑은 고딕", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.timeLabel.Location = new System.Drawing.Point(12, 540);
            this.timeLabel.Name = "timeLabel";
            this.timeLabel.Size = new System.Drawing.Size(80, 21);
            this.timeLabel.TabIndex = 5;
            this.timeLabel.Text = "오늘 날짜";
            // 
            // subTitleLabel
            // 
            this.subTitleLabel.AutoSize = true;
            this.subTitleLabel.Font = new System.Drawing.Font("맑은 고딕", 18F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.subTitleLabel.Location = new System.Drawing.Point(257, 66);
            this.subTitleLabel.Name = "subTitleLabel";
            this.subTitleLabel.Size = new System.Drawing.Size(271, 32);
            this.subTitleLabel.TabIndex = 8;
            this.subTitleLabel.Text = "Seoul booking System";
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.userPanel1);
            this.panel3.Controls.Add(this.adminPanel1);
            this.panel3.Location = new System.Drawing.Point(0, 101);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(792, 299);
            this.panel3.TabIndex = 9;
            // 
            // userPanel1
            // 
            this.userPanel1.Location = new System.Drawing.Point(0, 3);
            this.userPanel1.Name = "userPanel1";
            this.userPanel1.Size = new System.Drawing.Size(792, 296);
            this.userPanel1.TabIndex = 0;
            // 
            // button1
            // 
            this.button1.BackColor = System.Drawing.Color.White;
            this.button1.Font = new System.Drawing.Font("굴림", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.button1.Location = new System.Drawing.Point(669, 12);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(95, 29);
            this.button1.TabIndex = 10;
            this.button1.Text = "로그인";
            this.button1.UseVisualStyleBackColor = false;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // timer1
            // 
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // nameLabel
            // 
            this.nameLabel.AutoSize = true;
            this.nameLabel.Font = new System.Drawing.Font("맑은 고딕", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.nameLabel.Location = new System.Drawing.Point(696, 542);
            this.nameLabel.Name = "nameLabel";
            this.nameLabel.Size = new System.Drawing.Size(80, 21);
            this.nameLabel.TabIndex = 11;
            this.nameLabel.Text = "오늘 날짜";
            // 
            // imgPanel1
            // 
            this.imgPanel1.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.imgPanel1.Controls.Add(this.label1);
            this.imgPanel1.Location = new System.Drawing.Point(116, 406);
            this.imgPanel1.Name = "imgPanel1";
            this.imgPanel1.Size = new System.Drawing.Size(156, 104);
            this.imgPanel1.TabIndex = 2;
            // 
            // imgPanel2
            // 
            this.imgPanel2.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.imgPanel2.Controls.Add(this.label2);
            this.imgPanel2.Location = new System.Drawing.Point(338, 406);
            this.imgPanel2.Name = "imgPanel2";
            this.imgPanel2.Size = new System.Drawing.Size(156, 104);
            this.imgPanel2.TabIndex = 12;
            // 
            // imgPanel3
            // 
            this.imgPanel3.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.imgPanel3.Controls.Add(this.label3);
            this.imgPanel3.Location = new System.Drawing.Point(560, 406);
            this.imgPanel3.Name = "imgPanel3";
            this.imgPanel3.Size = new System.Drawing.Size(156, 104);
            this.imgPanel3.TabIndex = 13;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.BackColor = System.Drawing.Color.Transparent;
            this.label1.ForeColor = System.Drawing.Color.White;
            this.label1.Location = new System.Drawing.Point(12, 81);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(38, 12);
            this.label1.TabIndex = 0;
            this.label1.Text = "label1";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.BackColor = System.Drawing.Color.Transparent;
            this.label2.ForeColor = System.Drawing.Color.White;
            this.label2.Location = new System.Drawing.Point(18, 81);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(38, 12);
            this.label2.TabIndex = 1;
            this.label2.Text = "label2";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.BackColor = System.Drawing.Color.Transparent;
            this.label3.ForeColor = System.Drawing.Color.White;
            this.label3.Location = new System.Drawing.Point(13, 81);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(38, 12);
            this.label3.TabIndex = 2;
            this.label3.Text = "label3";
            // 
            // adminPanel1
            // 
            this.adminPanel1.Location = new System.Drawing.Point(0, 0);
            this.adminPanel1.Name = "adminPanel1";
            this.adminPanel1.Size = new System.Drawing.Size(792, 299);
            this.adminPanel1.TabIndex = 1;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(790, 574);
            this.Controls.Add(this.imgPanel3);
            this.Controls.Add(this.imgPanel2);
            this.Controls.Add(this.imgPanel1);
            this.Controls.Add(this.nameLabel);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.panel3);
            this.Controls.Add(this.subTitleLabel);
            this.Controls.Add(this.timeLabel);
            this.Controls.Add(this.panel2);
            this.Controls.Add(this.titleLabel);
            this.Controls.Add(this.titleImage);
            this.Controls.Add(this.panel1);
            this.HelpButton = true;
            this.Name = "Form1";
            this.Text = "메인";
            this.VisibleChanged += new System.EventHandler(this.Form1_VisibleChanged);
            ((System.ComponentModel.ISupportInitialize)(this.titleImage)).EndInit();
            this.panel3.ResumeLayout(false);
            this.imgPanel1.ResumeLayout(false);
            this.imgPanel1.PerformLayout();
            this.imgPanel2.ResumeLayout(false);
            this.imgPanel2.PerformLayout();
            this.imgPanel3.ResumeLayout(false);
            this.imgPanel3.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.PictureBox titleImage;
        private System.Windows.Forms.Label titleLabel;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Label timeLabel;
        private System.Windows.Forms.Label subTitleLabel;
        private System.Windows.Forms.Panel panel3;
        private System.Windows.Forms.Button button1;
        private userPanel userPanel1;
        private adminPanel adminPanel1;
        private System.Windows.Forms.Timer timer1;
        private System.Windows.Forms.Label nameLabel;
        private System.Windows.Forms.Panel imgPanel1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Panel imgPanel2;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Panel imgPanel3;
        private System.Windows.Forms.Label label3;
    }
}

