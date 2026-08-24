using System.Windows.Forms;

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
            this.u4 = new System.Windows.Forms.TextBox();
            this.u3 = new System.Windows.Forms.TextBox();
            this.u2 = new System.Windows.Forms.TextBox();
            this.u1 = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
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
            this.c1.Location = new System.Drawing.Point(12, 210);
            this.c1.Name = "c1";
            this.c1.Size = new System.Drawing.Size(429, 20);
            this.c1.TabIndex = 4;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 195);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(57, 12);
            this.label1.TabIndex = 5;
            this.label1.Text = "계약 상태";
            // 
            // n2
            // 
            this.n2.Location = new System.Drawing.Point(12, 171);
            this.n2.Name = "n2";
            this.n2.Size = new System.Drawing.Size(429, 21);
            this.n2.TabIndex = 6;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 156);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(62, 12);
            this.label2.TabIndex = 7;
            this.label2.Text = "출연료(\\)";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(12, 111);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(57, 12);
            this.label3.TabIndex = 9;
            this.label3.Text = "구성 인원";
            // 
            // n1
            // 
            this.n1.Location = new System.Drawing.Point(12, 126);
            this.n1.Name = "n1";
            this.n1.Size = new System.Drawing.Size(429, 21);
            this.n1.TabIndex = 8;
            // 
            // button1
            // 
            this.button1.BackColor = System.Drawing.Color.White;
            this.button1.Location = new System.Drawing.Point(123, 337);
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
            this.button2.Location = new System.Drawing.Point(223, 337);
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
            this.u4.Location = new System.Drawing.Point(14, 301);
            this.u4.Name = "u4";
            this.u4.Size = new System.Drawing.Size(429, 21);
            this.u4.TabIndex = 3;
            // 
            // u3
            // 
            this.u3.BackColor = System.Drawing.Color.White;
            this.u3.Location = new System.Drawing.Point(12, 253);
            this.u3.Name = "u3";
            this.u3.Size = new System.Drawing.Size(429, 21);
            this.u3.TabIndex = 2;
            // 
            // u2
            // 
            this.u2.BackColor = System.Drawing.Color.White;
            this.u2.Location = new System.Drawing.Point(12, 80);
            this.u2.Name = "u2";
            this.u2.Size = new System.Drawing.Size(429, 21);
            this.u2.TabIndex = 1;
            // 
            // u1
            // 
            this.u1.BackColor = System.Drawing.Color.White;
            this.u1.Location = new System.Drawing.Point(12, 32);
            this.u1.Name = "u1";
            this.u1.Size = new System.Drawing.Size(429, 21);
            this.u1.TabIndex = 0;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(12, 17);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(39, 12);
            this.label4.TabIndex = 12;
            this.label4.Text = "이름 *";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(12, 65);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(29, 12);
            this.label5.TabIndex = 13;
            this.label5.Text = "장르";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(12, 238);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(41, 12);
            this.label6.TabIndex = 14;
            this.label6.Text = "연락처";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(12, 286);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(41, 12);
            this.label7.TabIndex = 15;
            this.label7.Text = "이메일";
            // 
            // PerformUpdate
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(451, 380);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.label4);
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

        private TextBox u1;
        private TextBox u2;
        private TextBox u3;
        private TextBox u4;
        private System.Windows.Forms.ComboBox c1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.NumericUpDown n2;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.NumericUpDown n1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private Label label4;
        private Label label5;
        private Label label6;
        private Label label7;
    }
}