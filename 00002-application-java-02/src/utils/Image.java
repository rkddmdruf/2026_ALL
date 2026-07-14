package utils;

import javax.swing.ImageIcon;

public enum Image {
	PROFILE("profile"),
	REGION("region");
	
	public String string;
	
	Image(String string) { this.string  = string; }
	
	public ImageIcon getImage(String s, int w, int h) {
		String extension = "." + (string.equals(PROFILE.string) ? "jpg" : "png");
		return new ImageIcon(new ImageIcon("datafiles/" + string + "/" + s + extension).getImage().getScaledInstance(w, h, 4));
	}
}
