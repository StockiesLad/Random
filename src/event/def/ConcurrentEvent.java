package event.def;

import event.core.component.AbstractConcurrentEvent;
import event.core.interactive.EventArgs;
import event.core.interactive.EventStatus;
import event.core.interactive.StoredArgs;
import obj_holders.MutableObjectHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static event.core.interactive.EventStatus.*;

public class ConcurrentEvent<Context> extends AbstractConcurrentEvent<Context, ConcurrentEvent<Context>> {
    private final Map<String, StoredArgs<Context, ConcurrentEvent<Context>>>
            argsToAdd,
            allArgs,
            argsToRemove;

    public ConcurrentEvent(final Comparator<Long> eventOrder) {
        super(eventOrder);
        argsToAdd = allArgs = argsToRemove = new ConcurrentHashMap<>();
        setGenericLocation(List.of(argsToAdd, allArgs, argsToRemove));
    }

    public ConcurrentEvent() {
        this(Comparator.reverseOrder());
    }

    @Override
    public void clean() {
        super.clean();
        argsToAdd.clear();
        argsToRemove.clear();
    }

    @Override
    public Map<String, StoredArgs<Context, ConcurrentEvent<Context>>> getArgsStorage() {
        return allArgs;
    }

    @Override
    public synchronized void removeDynamic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context, ConcurrentEvent<Context>> eventArgs
    ) {
        for (final var string : locations) {
            argsToRemove.putIfAbsent(string, getNewArgsStorage());

            final var holder = argsToRemove.get(string);
            if (!holder.priorities().contains(priority))
                holder.priorities().add(priority);
            if (!holder.args().containsKey(priority)) {
                final ArrayList<EventArgs<Context, ConcurrentEvent<Context>>> list = new ArrayList<>();
                list.add(eventArgs);
                holder.args().put(priority, list);
            } else if (!holder.args().get(priority).contains(eventArgs))
                holder.args().get(priority).add(eventArgs);
        }
    }


    @Override
    public synchronized void registerDynamic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context, ConcurrentEvent<Context>> eventArgs
    ) {
        for (final var location : locations) {
            argsToAdd.putIfAbsent(location, getNewArgsStorage());
            final var holder = argsToAdd.get(location);

            if (!holder.priorities().contains(priority))
                holder.priorities().add(priority);
            if (!holder.args().containsKey(priority)) {
                final ArrayList<EventArgs<Context, ConcurrentEvent<Context>>> list = new ArrayList<>();
                list.add(eventArgs);
                holder.args().put(priority, list);
            } else if (!holder.args().get(priority).contains(eventArgs))
                holder.args().get(priority).add(eventArgs);
        }
    }

    @Override
    public synchronized void removeStatic(
            final long priority,
            final List<String> locations,
            final EventArgs<Context, ConcurrentEvent<Context>> eventArgs
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
            final EventArgs<Context, ConcurrentEvent<Context>> eventArgs
    ) {
        for (final var location : locations) {
            allArgs.putIfAbsent(location, getNewArgsStorage());
            final StoredArgs<Context, ConcurrentEvent<Context>> holder = allArgs.get(location);
            final var priHol = holder.priorities();
            final var argHol = holder.args();

            if (!getAllLocations().contains(location))
                getAllLocations().add(location);
            if (!priHol.contains(priority))
                priHol.add(priority);
            if (!argHol.containsKey(priority)) {
                final ArrayList<EventArgs<Context, ConcurrentEvent<Context>>> list = new ArrayList<>();
                list.add(eventArgs);
                argHol.put(priority, list);
            } else if (!argHol.get(priority).contains(eventArgs))
                argHol.get(priority).add(eventArgs);
        }
    }

    @Override
    public void execute(final List<String> locations, final boolean orderFlipped, final Context eventContext) {
        synchronized (argsToAdd) {
            synchronized (argsToRemove) {
                MutableObjectHolder<EventStatus> statusHolder = new MutableObjectHolder<>(CONTINUE);
                for (final var string : locations) {
                    synchronized (getArgsStorage().get(string)) {
                        final var add = argsToAdd.get(string);
                        final var all = allArgs.get(string);
                        final var remove = argsToRemove.remove(string);
                        final Comparator<Long> order;

                        if (orderFlipped)
                            order = this.getOrder().reversed();
                        else order = this.getOrder();

                        if (add != null) {
                            iterateArgsStorage(add, this::registerStatic);
                            add.clear();
                        }

                        all.priorities().sort(order);
                        if (!statusHolder.getHeldObj().equals(FINISH_ALL))
                            for (long priority : all.priorities()) {
                                for (EventArgs<Context, ConcurrentEvent<Context>> arg : all.args().get(priority)) {
                                    arg.recursive(eventContext, statusHolder, this, 0, arg);
                                    if (statusHolder.equalsAny(List.of(FINISH_ALL, FINISH_LOCATION, FINISH_PRIORITY)))
                                        break;
                                }
                                if (statusHolder.equalsAny(List.of(FINISH_ALL, FINISH_LOCATION)))
                                    break;
                            }

                        if (remove != null) {
                            iterateArgsStorage(remove, this::removeStatic);
                            remove.clear();
                        }

                        clean(all);
                    }
                }
                argsToAdd.clear();
                argsToRemove.clear();
            }
        }
    }
}
