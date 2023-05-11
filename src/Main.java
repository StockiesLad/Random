import event.core.AbstractEvent;
import event.def.ConcurrentEvent;
import obj_holders.MutableObjectHolder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        new Printer(){}.Constructor(
                List.of(new Printer.Pair("string", "Hi, this is an interface pretending to be an object"))
        ).print();
        System.out.println(WomanDefinition.getWoman(true));

        for (int t = 0; t < 100; t++) {
            final int z = t;
            final AbstractEvent<MutableObjectHolder<Integer>> event = new ConcurrentEvent<>();
            for (int i = 0; i < 100; i++) {
                final int n = i;
                final Thread thread = new Thread(() -> event.registerListener(1,
                        (eventContext, eventStatus, event1, eventArgs) -> System.out.println(z * 100 + n))
                );
                thread.start();
            }
            for (int i = 0; i < 1; i++) {
                final Thread thread = new Thread(() -> event.execute(null));
                thread.start();
            }
        }
    }
}