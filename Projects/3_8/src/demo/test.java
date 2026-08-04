package demo;

import orms.ProjectEntity;

public class test {

	public static void main(String[] args) {
		System.out.println(ProjectEntity.findById(2).get());
	}
}
