package event.def;

import event.core.component.AbstractConcurrentEvent;
import event.core.interactive.EventArgs;
import event.core.interactive.StoredArgs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmptyEvent is used to debug events by essentially turning them off by assigning them this class.
 */
@SuppressWarnings("unused")
public class EmptyConcurrentEvent extends AbstractConcurrentEvent<Object, EmptyConcurrentEvent> {

    private final Map<String, StoredArgs<Object, EmptyConcurrentEvent>> map;

    public EmptyConcurrentEvent() {
        super(Comparator.reverseOrder());
        map = new HashMap<>();
        setGenericLocation(List.of(map));
    }

    @Override
    public void removeDynamic(long priority, List<String> locations, EventArgs<Object, EmptyConcurrentEvent> eventArgs) {

    }

    @Override
    public void registerDynamic(long priority, List<String> locations, EventArgs<Object, EmptyConcurrentEvent> eventArgs) {

    }

    @Override
    public void execute(List<String> locations, boolean orderFlipped, Object eventContext) {

    }

    @Override
    public Map<String, StoredArgs<Object, EmptyConcurrentEvent>> getArgsStorage() {
        return map;
    }

    @Override
    public void removeStatic(long priority, List<String> locations, EventArgs<Object, EmptyConcurrentEvent> eventArgs) {

    }

    @Override
    public void registerStatic(long priority, List<String> locations, EventArgs<Object, EmptyConcurrentEvent> eventArgs) {

    }
}
