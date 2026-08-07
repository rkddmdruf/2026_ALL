namespace _1_5 {
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
            this.login = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            this.panel2 = new System.Windows.Forms.Panel();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.topPanel1 = new _1_5.topPanel();
            this.img3 = new System.Windows.Forms.Panel();
            this.imgLabel3 = new System.Windows.Forms.Label();
            this.img2 = new System.Windows.Forms.Panel();
            this.imgLabel2 = new System.Windows.Forms.Label();
            this.img1 = new System.Windows.Forms.Panel();
            this.imgLabel1 = new System.Windows.Forms.Label();
            this.label9 = new System.Windows.Forms.Label();
            this.panel1.SuspendLayout();
            this.panel2.SuspendLayout();
            this.img3.SuspendLayout();
            this.img2.SuspendLayout();
            this.img1.SuspendLayout();
            this.SuspendLayout();
            // 
            // login
            // 
            this.login.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.login.Font = new System.Drawing.Font("맑은 고딕", 11.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.login.Location = new System.Drawing.Point(578, 11);
            this.login.Name = "login";
            this.login.Size = new System.Drawing.Size(94, 29);
            this.login.TabIndex = 1;
            this.login.Text = "button1";
            this.login.UseVisualStyleBackColor = true;
            this.login.Click += new System.EventHandler(this.login_Click);
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.Color.Black;
            this.panel1.Controls.Add(this.panel2);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panel1.Location = new System.Drawing.Point(0, 479);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(684, 60);
            this.panel1.TabIndex = 2;
            // 
            // panel2
            // 
            this.panel2.BackColor = System.Drawing.Color.White;
            this.panel2.Controls.Add(this.label2);
            this.panel2.Controls.Add(this.label1);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panel2.Location = new System.Drawing.Point(0, 3);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(684, 57);
            this.panel2.TabIndex = 3;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("맑은 고딕", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label2.Location = new System.Drawing.Point(583, 17);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(65, 25);
            this.label2.TabIndex = 1;
            this.label2.Text = "label2";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("맑은 고딕", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(23, 17);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(65, 25);
            this.label1.TabIndex = 0;
            this.label1.Text = "label1";
            // 
            // topPanel1
            // 
            this.topPanel1.BackColor = System.Drawing.Color.Black;
            this.topPanel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.topPanel1.Location = new System.Drawing.Point(0, 0);
            this.topPanel1.Name = "topPanel1";
            this.topPanel1.Size = new System.Drawing.Size(684, 53);
            this.topPanel1.TabIndex = 0;
            // 
            // img3
            // 
            this.img3.BackColor = System.Drawing.Color.White;
            this.img3.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.img3.Controls.Add(this.imgLabel3);
            this.img3.Location = new System.Drawing.Point(474, 366);
            this.img3.Name = "img3";
            this.img3.Size = new System.Drawing.Size(167, 100);
            this.img3.TabIndex = 27;
            // 
            // imgLabel3
            // 
            this.imgLabel3.AutoSize = true;
            this.imgLabel3.BackColor = System.Drawing.Color.White;
            this.imgLabel3.ForeColor = System.Drawing.Color.White;
            this.imgLabel3.Location = new System.Drawing.Point(3, 86);
            this.imgLabel3.Name = "imgLabel3";
            this.imgLabel3.Size = new System.Drawing.Size(44, 12);
            this.imgLabel3.TabIndex = 2;
            this.imgLabel3.Text = "label10";
            // 
            // img2
            // 
            this.img2.BackColor = System.Drawing.Color.White;
            this.img2.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.img2.Controls.Add(this.imgLabel2);
            this.img2.Location = new System.Drawing.Point(269, 366);
            this.img2.Name = "img2";
            this.img2.Size = new System.Drawing.Size(167, 100);
            this.img2.TabIndex = 28;
            // 
            // imgLabel2
            // 
            this.imgLabel2.AutoSize = true;
            this.imgLabel2.ForeColor = System.Drawing.Color.White;
            this.imgLabel2.Location = new System.Drawing.Point(3, 86);
            this.imgLabel2.Name = "imgLabel2";
            this.imgLabel2.Size = new System.Drawing.Size(44, 12);
            this.imgLabel2.TabIndex = 1;
            this.imgLabel2.Text = "label10";
            // 
            // img1
            // 
            this.img1.BackColor = System.Drawing.Color.White;
            this.img1.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.img1.Controls.Add(this.imgLabel1);
            this.img1.Location = new System.Drawing.Point(64, 366);
            this.img1.Name = "img1";
            this.img1.Size = new System.Drawing.Size(167, 100);
            this.img1.TabIndex = 26;
            // 
            // imgLabel1
            // 
            this.imgLabel1.AutoSize = true;
            this.imgLabel1.ForeColor = System.Drawing.Color.White;
            this.imgLabel1.Location = new System.Drawing.Point(3, 86);
            this.imgLabel1.Name = "imgLabel1";
            this.imgLabel1.Size = new System.Drawing.Size(44, 12);
            this.imgLabel1.TabIndex = 0;
            this.imgLabel1.Text = "label10";
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Font = new System.Drawing.Font("맑은 고딕", 15.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label9.ForeColor = System.Drawing.Color.Red;
            this.label9.Location = new System.Drawing.Point(33, 330);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(166, 30);
            this.label9.TabIndex = 25;
            this.label9.Text = "•인기 호텔 top3";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(684, 539);
            this.Controls.Add(this.img3);
            this.Controls.Add(this.img2);
            this.Controls.Add(this.img1);
            this.Controls.Add(this.label9);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.login);
            this.Controls.Add(this.topPanel1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Form1";
            this.Text = "메인";
            this.VisibleChanged += new System.EventHandler(this.Form1_VisibleChanged);
            this.panel1.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.panel2.PerformLayout();
            this.img3.ResumeLayout(false);
            this.img3.PerformLayout();
            this.img2.ResumeLayout(false);
            this.img2.PerformLayout();
            this.img1.ResumeLayout(false);
            this.img1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private topPanel topPanel1;
        private System.Windows.Forms.Button login;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Panel img3;
        private System.Windows.Forms.Label imgLabel3;
        private System.Windows.Forms.Panel img2;
        private System.Windows.Forms.Label imgLabel2;
        private System.Windows.Forms.Panel img1;
        private System.Windows.Forms.Label imgLabel1;
        private System.Windows.Forms.Label label9;
    }
}

