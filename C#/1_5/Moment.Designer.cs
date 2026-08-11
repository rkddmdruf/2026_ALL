namespace _1_5 {
    partial class Moment {
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Moment));
            this.topPanel1 = new _1_5.topPanel();
            this.left = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.right = new System.Windows.Forms.Label();
            this.hotelLabel = new System.Windows.Forms.Label();
            this.button1 = new System.Windows.Forms.Button();
            this.p1 = new System.Windows.Forms.TableLayoutPanel();
            this.SuspendLayout();
            // 
            // topPanel1
            // 
            this.topPanel1.BackColor = System.Drawing.Color.Black;
            this.topPanel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.topPanel1.Location = new System.Drawing.Point(0, 0);
            this.topPanel1.Name = "topPanel1";
            this.topPanel1.Size = new System.Drawing.Size(808, 53);
            this.topPanel1.TabIndex = 0;
            // 
            // left
            // 
            this.left.AutoSize = true;
            this.left.Enabled = false;
            this.left.Font = new System.Drawing.Font("굴림", 11.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.left.Location = new System.Drawing.Point(38, 72);
            this.left.Name = "left";
            this.left.Size = new System.Drawing.Size(18, 15);
            this.left.TabIndex = 1;
            this.left.Text = "<";
            this.left.Click += new System.EventHandler(this.left_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("굴림", 11.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(391, 72);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(32, 15);
            this.label1.TabIndex = 2;
            this.label1.Text = "8월";
            // 
            // right
            // 
            this.right.AutoSize = true;
            this.right.Font = new System.Drawing.Font("굴림", 11.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.right.Location = new System.Drawing.Point(751, 72);
            this.right.Name = "right";
            this.right.Size = new System.Drawing.Size(18, 15);
            this.right.TabIndex = 3;
            this.right.Text = ">";
            this.right.Click += new System.EventHandler(this.right_Click);
            // 
            // hotelLabel
            // 
            this.hotelLabel.AutoSize = true;
            this.hotelLabel.Font = new System.Drawing.Font("굴림", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.hotelLabel.Location = new System.Drawing.Point(37, 454);
            this.hotelLabel.Name = "hotelLabel";
            this.hotelLabel.Size = new System.Drawing.Size(40, 19);
            this.hotelLabel.TabIndex = 4;
            this.hotelLabel.Text = "8월";
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(624, 454);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(164, 22);
            this.button1.TabIndex = 5;
            this.button1.Text = "확인";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // p1
            // 
            this.p1.ColumnCount = 1;
            this.p1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.p1.Location = new System.Drawing.Point(12, 102);
            this.p1.Name = "p1";
            this.p1.RowCount = 1;
            this.p1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.p1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 346F));
            this.p1.Size = new System.Drawing.Size(776, 346);
            this.p1.TabIndex = 6;
            // 
            // Moment
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(808, 488);
            this.Controls.Add(this.p1);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.hotelLabel);
            this.Controls.Add(this.right);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.left);
            this.Controls.Add(this.topPanel1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Moment";
            this.Text = "날짜 선택";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private topPanel topPanel1;
        private System.Windows.Forms.Label left;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label right;
        private System.Windows.Forms.Label hotelLabel;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.TableLayoutPanel p1;
    }
}