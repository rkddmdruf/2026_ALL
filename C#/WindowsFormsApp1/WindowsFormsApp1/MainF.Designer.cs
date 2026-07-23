namespace WindowsFormsApp1 {
    partial class MainF {
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
            this.panel1 = new System.Windows.Forms.Panel();
            this.bLabel = new System.Windows.Forms.Label();
            this.buttonPanel = new System.Windows.Forms.Panel();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.panel1.BackColor = System.Drawing.SystemColors.ControlLight;
            this.panel1.ForeColor = System.Drawing.SystemColors.ControlText;
            this.panel1.Location = new System.Drawing.Point(212, 47);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(834, 536);
            this.panel1.TabIndex = 0;
            // 
            // bLabel
            // 
            this.bLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.bLabel.BackColor = System.Drawing.SystemColors.ControlDark;
            this.bLabel.Location = new System.Drawing.Point(270, 630);
            this.bLabel.Name = "bLabel";
            this.bLabel.Size = new System.Drawing.Size(834, 21);
            this.bLabel.TabIndex = 1;
            this.bLabel.Text = "• 연결됨   DB:festivaldb    v0.1.0";
            // 
            // buttonPanel
            // 
            this.buttonPanel.BackColor = System.Drawing.SystemColors.Control;
            this.buttonPanel.ForeColor = System.Drawing.SystemColors.ControlText;
            this.buttonPanel.Location = new System.Drawing.Point(0, 1);
            this.buttonPanel.Name = "buttonPanel";
            this.buttonPanel.Size = new System.Drawing.Size(93, 363);
            this.buttonPanel.TabIndex = 2;
            // 
            // MainF
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1100, 650);
            this.Controls.Add(this.buttonPanel);
            this.Controls.Add(this.bLabel);
            this.Controls.Add(this.panel1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.Name = "MainF";
            this.Text = "MainF";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Label bLabel;
        private System.Windows.Forms.Panel buttonPanel;
    }
}