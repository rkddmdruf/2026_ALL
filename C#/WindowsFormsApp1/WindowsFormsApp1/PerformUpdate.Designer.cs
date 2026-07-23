namespace WindowsFormsApp1 {
    partial class PerformUpdate {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            this.c1 = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.n2 = new System.Windows.Forms.NumericUpDown();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.n1 = new System.Windows.Forms.NumericUpDown();
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.u4 = new WindowsFormsApp1.UserText();
            this.u3 = new WindowsFormsApp1.UserText();
            this.u2 = new WindowsFormsApp1.UserText();
            this.u1 = new WindowsFormsApp1.UserText();
            ((System.ComponentModel.ISupportInitialize)(this.n2)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.n1)).BeginInit();
            this.SuspendLayout();
            // 
            // c1
            // 
            this.c1.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.c1.FormattingEnabled = true;
            this.c1.Items.AddRange(new object[] {
            "계약완료",
            "조율중",
            "취소"});
            this.c1.Location = new System.Drawing.Point(12, 226);
            this.c1.Name = "c1";
            this.c1.Size = new System.Drawing.Size(429, 20);
            this.c1.TabIndex = 4;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 211);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(57, 12);
            this.label1.TabIndex = 5;
            this.label1.Text = "계약 상태";
            // 
            // n2
            // 
            this.n2.Location = new System.Drawing.Point(12, 187);
            this.n2.Name = "n2";
            this.n2.Size = new System.Drawing.Size(429, 21);
            this.n2.TabIndex = 6;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 172);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(62, 12);
            this.label2.TabIndex = 7;
            this.label2.Text = "출연료(\\)";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(12, 127);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(57, 12);
            this.label3.TabIndex = 9;
            this.label3.Text = "구성 인원";
            // 
            // n1
            // 
            this.n1.Location = new System.Drawing.Point(12, 142);
            this.n1.Name = "n1";
            this.n1.Size = new System.Drawing.Size(429, 21);
            this.n1.TabIndex = 8;
            // 
            // button1
            // 
            this.button1.BackColor = System.Drawing.Color.White;
            this.button1.Location = new System.Drawing.Point(122, 374);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(94, 33);
            this.button1.TabIndex = 10;
            this.button1.Text = "취소";
            this.button1.UseVisualStyleBackColor = false;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.BackColor = System.Drawing.SystemColors.MenuHighlight;
            this.button2.ForeColor = System.Drawing.Color.White;
            this.button2.Location = new System.Drawing.Point(222, 374);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(96, 33);
            this.button2.TabIndex = 11;
            this.button2.Text = "저장";
            this.button2.UseVisualStyleBackColor = false;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // u4
            // 
            this.u4.BackColor = System.Drawing.Color.White;
            this.u4.Location = new System.Drawing.Point(12, 308);
            this.u4.Name = "u4";
            this.u4.Size = new System.Drawing.Size(429, 50);
            this.u4.TabIndex = 3;
            // 
            // u3
            // 
            this.u3.BackColor = System.Drawing.Color.White;
            this.u3.Location = new System.Drawing.Point(12, 252);
            this.u3.Name = "u3";
            this.u3.Size = new System.Drawing.Size(429, 50);
            this.u3.TabIndex = 2;
            // 
            // u2
            // 
            this.u2.BackColor = System.Drawing.Color.White;
            this.u2.Location = new System.Drawing.Point(12, 68);
            this.u2.Name = "u2";
            this.u2.Size = new System.Drawing.Size(429, 50);
            this.u2.TabIndex = 1;
            // 
            // u1
            // 
            this.u1.BackColor = System.Drawing.Color.White;
            this.u1.Location = new System.Drawing.Point(12, 12);
            this.u1.Name = "u1";
            this.u1.Size = new System.Drawing.Size(429, 50);
            this.u1.TabIndex = 0;
            // 
            // PerformUpdate
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(451, 417);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.n1);
            this.Controls.Add(this.n2);
            this.Controls.Add(this.c1);
            this.Controls.Add(this.u4);
            this.Controls.Add(this.u3);
            this.Controls.Add(this.u2);
            this.Controls.Add(this.u1);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Name = "PerformUpdate";
            this.Text = "새 공연자 등록";
            ((System.ComponentModel.ISupportInitialize)(this.n2)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.n1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private UserText u1;
        private UserText u2;
        private UserText u3;
        private UserText u4;
        private System.Windows.Forms.ComboBox c1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.NumericUpDown n2;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.NumericUpDown n1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
    }
}