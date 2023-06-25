import event.core.AbstractEvent;
import event.def.ConcurrentEvent;
import obj_holders.MutableObjectHolder;
import obj_holders.ObjectHolder;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        new Printer(){}.Constructor(
                List.of(new Printer.Pair("string", "Hi, this is an interface pretending to be an object"))
        ).print();
        System.out.println(WomanDefinition.getWoman(true));

        final AbstractEvent<ObjectHolder<Integer>> event = new ConcurrentEvent<>();
        Random random = new Random();
        event.registerListener(-1, 1 + "a",
                (eventContext, eventStatus, event1, eventArgs) -> System.out.println(-1));
        for (int i = 0; i < 10; i++) {
            final int n = random.nextInt(5);
            final Thread thread = new Thread(() -> {
                event.registerListener(n, n * n + "abc",
                        (eventContext, eventStatus, event1, eventArgs) -> System.out.println(n));
            });
            thread.start();

        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 3; i++) {
            final Thread thread = new Thread(() -> {
                event.getRegistry().args.forEach((s, longQueueConcurrentSkipListMap) -> {
                    System.out.println(s);
                    event.execute(s, new MutableObjectHolder<>(1));
                });
            });
            thread.start();
        }
    }
}