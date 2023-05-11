package event.def;

import event.core.AbstractEvent;
import event.core.EventArgs;
import event.core.EventRegistry;
import event.core.EventStatus;
import obj_holders.MutableObjectHolder;

import java.util.Comparator;
import java.util.List;

import static event.core.EventStatus.*;


public class ConcurrentEvent<Context> implements AbstractEvent<Context> {
    private final EventRegistry<Context> eventRegistry;

    public ConcurrentEvent(final Comparator<Long> eventOrder) {
        eventRegistry = new EventRegistry<>(eventOrder);
    }

    public ConcurrentEvent() {
        this(Comparator.reverseOrder());
    }



    @Override
    public EventRegistry<Context> getRegistry() {
        return eventRegistry;
    }

    @Override
    public void removeListener(long priority, String location, EventArgs<Context> eventArgs) {
        eventRegistry.remove(location, priority, eventArgs);
    }

    @Override
    public void registerListener(long priority, String location, EventArgs<Context> eventArgs) {
        eventRegistry.add(location, priority, eventArgs);
    }

    @Override
    public void execute(final String location, final boolean orderFlipped, final Context eventContext) {
        MutableObjectHolder<EventStatus> statusHolder = new MutableObjectHolder<>(CONTINUE);
        final var all = eventRegistry.args.get(location);
        synchronized (all) {
            for(final var priority : all.priorities) {
                for (EventArgs<Context> eventArgs : all.priorityMap.get(priority)) {
                    eventArgs.recursive(eventContext, statusHolder, this, 0, eventArgs);
                    if (statusHolder.equalsAny(List.of(FINISH_PRIORITY, FINISH_LOCATION)))
                        break;
                }
                if (!statusHolder.equalsAny(List.of(FINISH_LOCATION)))
                    break;
            }
        }
    }
}