package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import orms.*;

public class test extends CFrame {
	public test() {
		setFrame("sdfds", 500, 500);
	}

	protected void desing() {
		CTree t = new CTree();
		categoryEntity.findAll().forEach(e -> {
			t.Values(e.cname, detailEntity.findBy(c -> c.cno.equals(e.cno)).stream().map(c -> c.dname).toArray(String[]::new));
		});
		categoryEntity.findAll().forEach(e -> {
			t.Values(e.cname, detailEntity.findBy(c -> c.cno.equals(e.cno)).stream().map(c -> c.dname).toArray(String[]::new));
		});
		add(new JScrollPane(t));
	}

	protected void action() {
	}
	
	public static void main(String[] args) {
		Util.start(new test());
	}
}