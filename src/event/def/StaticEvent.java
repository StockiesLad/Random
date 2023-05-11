package event.def;

import event.core.component.AbstractStaticEvent;
import event.core.interactive.EventArgs;
import event.core.interactive.EventConstants;
import event.core.interactive.EventStatus;
import event.core.interactive.StoredArgs;
import obj_holders.MutableObjectHolder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static event.core.interactive.EventStatus.*;

public class StaticEvent<Context> extends AbstractStaticEvent<Context, StaticEvent<Context>> {
    private final Queue<String> allLocations;
    private final Map<String, StoredArgs<Context, StaticEvent<Context>>> allArgs;

    public StaticEvent(final Comparator<Long> eventOrder) {
        super(eventOrder);
        allLocations = new ConcurrentLinkedQueue<>();
        allArgs = new ConcurrentHashMap<>();
        allArgs.put(EventConstants.GENERIC_LOCATION, getNewArgsStorage());
    }

    public StaticEvent() {
        this(Comparator.reverseOrder());
    }

    @Override
    public Queue<String> getAllLocations() {
        return allLocations;
    }

    @Override
    public void clean() {
        for (final var location : getAllLocations()) {
            clean(allArgs.get(location));
        }
    }

    @Override
    public Map<String, StoredArgs<Context, StaticEvent<Context>>> getArgsStorage() {
        return allArgs;
    }

    @Override
    public synchronized void removeStatic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context, StaticEvent<Context>> eventArgs
    ) {
        for (final var string : locations) {
            final var holder = getArgsStorage().get(string);
            holder.args().get(priority).remove(eventArgs);
            if (holder.args().get(priority).isEmpty()) {
                holder.args().remove(priority);
                holder.priorities().remove(priority);
            }

        }
    }

    @Override
    public synchronized void registerStatic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context, StaticEvent<Context>> eventArgs
    ) {
        for (final var location : locations) {
            allArgs.putIfAbsent(location, getNewArgsStorage());
            final StoredArgs<Context, StaticEvent<Context>> holder = allArgs.get(location);
            final var priHol = holder.priorities();
            final var argHol = holder.args();

            if (!allLocations.contains(location))
                allLocations.add(location);
            if (!priHol.contains(priority))
                priHol.add(priority);
            if (!argHol.containsKey(priority)) {
                final ArrayList<EventArgs<Context, StaticEvent<Context>>> list = new ArrayList<>();
                list.add(eventArgs);
                argHol.put(priority, list);
            } else if (!argHol.get(priority).contains(eventArgs))
                argHol.get(priority).add(eventArgs);
        }
    }

    @Override
    public void execute(List<String> locations, boolean orderFlipped, Context eventContext) {
        MutableObjectHolder<EventStatus> statusHolder = new MutableObjectHolder<>(CONTINUE);
        for (final var string : locations) {
            synchronized (allArgs.get(string)) {
                final var all = allArgs.get(string);
                final Comparator<Long> order;

                if (orderFlipped)
                    order = getOrder().reversed();
                else order = getOrder();

                all.priorities().sort(order);
                if (!statusHolder.getHeldObj().equals(FINISH_ALL))
                    for (long priority : all.priorities()) {
                        for (EventArgs<Context, StaticEvent<Context>> arg : all.args().get(priority)) {
                            arg.recursive(eventContext, statusHolder, this, 0, arg);
                            if (statusHolder.equalsAny(List.of(FINISH_ALL, FINISH_LOCATION, FINISH_PRIORITY)))
                                break;
                        }
                        if (statusHolder.equalsAny(List.of(FINISH_ALL, FINISH_LOCATION)))
                            break;
                    }

                clean(all);
            }
        }
    }

    @Override
    public void removeListener(long priority, List<String> locations, EventArgs<Context, StaticEvent<Context>> eventArgs) {
        removeStatic(priority, locations, eventArgs);
    }

    @Override
    public void registerListener(long priority, List<String> locations, EventArgs<Context, StaticEvent<Context>> eventArgs) {
        registerStatic(priority, locations, eventArgs);
    }
}
