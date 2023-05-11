import event.core.component.AbstractEvent;
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
            final AbstractEvent<MutableObjectHolder<Integer>, ?> event =
                    new ConcurrentEvent<>();
            final MutableObjectHolder<Integer> objectHolder = new MutableObjectHolder<>(0);
            for (int i = 0; i < 1000; i++) {
                final Thread thread = new Thread(() -> event.registerListener(1,
                        (eventContext, eventStatus, event1, eventArgs) ->
                                objectHolder.setHeldObj(objectHolder.getHeldObj() + 1))
                );
                thread.start();
            }
            for (int i = 0; i < 1000; i++) {
                final Thread thread = new Thread(() -> event.execute(null));
                thread.start();
            }
            System.out.println(objectHolder.getHeldObj());
        }
    }
}