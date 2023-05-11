package event.def;

import event.core.component.AbstractStaticEvent;
import event.core.interactive.EventArgs;
import event.core.interactive.StoredArgs;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * EmptyEvent is used to debug events by essentially turning them off by assigning them this class.
 */
@SuppressWarnings("unused")
public class EmptyStaticEvent extends AbstractStaticEvent<Object, EmptyStaticEvent> {

    private final Map<String, StoredArgs<Object, EmptyStaticEvent>> map;
    private final Queue<String> allLocations;

    public EmptyStaticEvent() {
        super(Comparator.reverseOrder());
        map = new HashMap<>();
        allLocations = new ConcurrentLinkedQueue<>();
        setGenericLocation(List.of(map));
    }

    @Override
    public Queue<String> getAllLocations() {
        return allLocations;
    }

    @Override
    protected void clean() {

    }

    @Override
    public Map<String, StoredArgs<Object, EmptyStaticEvent>> getArgsStorage() {
        return map;
    }

    @Override
    public void execute(List<String> locations, boolean orderFlipped, Object eventContext) {

    }

    @Override
    public void removeListener(long priority, List<String> locations, EventArgs<Object, EmptyStaticEvent> eventArgs) {

    }

    @Override
    public void registerListener(long priority, List<String> locations, EventArgs<Object, EmptyStaticEvent> eventArgs) {

    }

    @Override
    public void removeStatic(long priority, List<String> locations, EventArgs<Object, EmptyStaticEvent> eventArgs) {

    }

    @Override
    public void registerStatic(long priority, List<String> locations, EventArgs<Object, EmptyStaticEvent> eventArgs) {

    }
}
