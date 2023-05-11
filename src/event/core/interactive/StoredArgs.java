package event.core.interactive;

import event.core.component.AbstractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record StoredArgs<Context, Event extends AbstractEvent<Context, Event>> (
        Map<Long, List<EventArgs<Context, Event>>> args,
        List<Long> priorities
) {
    public List<Long> copyPriorities() {
        return new ArrayList<>(priorities);
    }

    public Map<Long, List<EventArgs<Context, Event>>> copyArgs() {
        final Map<Long, List<EventArgs<Context, Event>>> args = new HashMap<>();
        this.args.forEach((aLong, eventArgs) -> args.put(aLong, new ArrayList<>(eventArgs)));
        return args;
    }

    public StoredArgs<Context, Event> copyHolder() {
        return new StoredArgs<>(copyArgs(), copyPriorities());
    }

    public void clear() {
        args.clear();
        priorities.clear();
    }
}