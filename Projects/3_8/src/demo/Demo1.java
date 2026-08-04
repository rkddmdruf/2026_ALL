package demo;

import orms.ProjectEntity;

public class Demo1 {
    public static void main(String[] args) {
        ProjectEntity p = ProjectEntity.findById(1).orElseThrow();

        System.out.println("pno = " + p.pno);

        for (ProjectEntity.Capacity c : p.capacities) {
            System.out.println("capacity: " + c.value + " / " + c.price + "원");
        }
        for (ProjectEntity.CarrierItem it : p.items) {
            System.out.println("item: " + it.type + " / " + it.price + "원");
        }
        for (ProjectEntity.Installment ins : p.installments) {
            System.out.println("installment: " + ins.month + "개월");
        }
    }
}
