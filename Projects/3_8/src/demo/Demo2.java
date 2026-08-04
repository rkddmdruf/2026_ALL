package demo;

import orms.ProjectEntity;
import orms.ProjectEntity.Capacity;
import orms.ProjectEntity.CarrierItem;
import orms.ProjectEntity.Installment;

import java.util.List;

public class Demo2 {
    public static void main(String[] args) {
        ProjectEntity p = new ProjectEntity();

        Capacity c = new Capacity();
        c.value = "256";
        c.price = 90000;
        p.capacities = List.of(c);

        CarrierItem it = new CarrierItem();
        it.type = "SKT";
        it.price = 99000;
        p.items = List.of(it);

        Installment ins = new Installment();
        ins.month = 24;
        p.installments = List.of(ins);

        p.save();
        System.out.println("saved as pno = " + p.pno);

        ProjectEntity.reload();
        System.out.println("reloaded: " + ProjectEntity.findById(p.pno).orElseThrow());
    }
}
