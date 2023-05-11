package event.core.component;

import event.core.interactive.EventArgs;

import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static event.core.interactive.EventConstants.GENERIC_LOCATION;

public abstract class AbstractConcurrentEvent<Context, Event extends AbstractEvent<Context, Event>>
        extends AbstractStaticEvent<Context, Event>
        implements AbstractDynamicEventRegistry<Context, Event>
{

    private final Queue<String> allLocations;

    public AbstractConcurrentEvent(Comparator<Long> eventOrder) {
        super(eventOrder);
        allLocations = new ConcurrentLinkedQueue<>();
        allLocations.add(GENERIC_LOCATION);
    }

    @Override
    public Queue<String> getAllLocations() {
        return allLocations;
    }

    @Override
    public void clean() {
        for (final var location : getAllLocations()) {
            clean(getArgsStorage().get(location));
        }
    }

    @Override
    public void removeListener(long priority, List<String> locations, EventArgs<Context, Event> eventArgs) {
        removeDynamic(priority, locations, eventArgs);
    }

    @Override
    public void registerListener(long priority, List<String> locations, EventArgs<Context, Event> eventArgs) {
        registerDynamic(priority, locations, eventArgs);

    }
}
