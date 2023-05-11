package event.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventRegistry<Context>{

    public final Map<String, PriorityHandler<Context>> args;
    public final Queue<String> locations;
    public Comparator<Long> comparator;

    public EventRegistry() {
        this(Comparator.reverseOrder());
    }

    public EventRegistry(Comparator<Long> comparator) {
        this.comparator = comparator;
        args = new ConcurrentHashMap<>();
        locations = new ConcurrentLinkedQueue<>();
    }

    public boolean isEmpty() {
        if (args.isEmpty())
            return true;
        else for (final var location : locations) {
            if (!isEmpty(args.get(location))) {
                return false;
            }
        }
        return false;
    }

    public boolean isEmpty(PriorityHandler<Context> priorityHandler) {
        if (priorityHandler == null)
            return true;
        if (locations.isEmpty())
            return true;
        else {
            for (long priority : priorityHandler.priorities) {
                if (!isEmpty(priorityHandler.priorityMap.get(priority)))
                    return false;
            }
        }
        return false;
    }

    public boolean isEmpty(Queue<EventArgs<Context>> priorities) {
        if (priorities == null)
            return true;
        else return priorities.isEmpty();
    }

    public void clean() {
        final List<String> emptyLocations = new ArrayList<>();
        for (final var location : locations) {
            final var priorityHandler = args.get(location);
            final List<Long> emptyPriorities = new ArrayList<>();
            for (final var priority : priorityHandler.priorities) {
                final var eventArgs = priorityHandler.priorityMap.get(priority);
                if (isEmpty(eventArgs)) {
                    priorityHandler.priorityMap.remove(priority);
                    emptyPriorities.add(priority);
                }
            }
            for (long emptyPriority : emptyPriorities)
                priorityHandler.priorities.remove(emptyPriority);
            if (isEmpty(priorityHandler))
                emptyLocations.add(location);
        }
        for (final var location : emptyLocations)
            args.remove(location);
    }

    public void remove(String location, long priority, EventArgs<Context> eventArgs) {
        args.get(location).priorityMap.get(priority).remove(eventArgs);
        clean();
    }

    public void add(String location, long priority, EventArgs<Context> eventArgs) {
        locations.add(location);
        if (args.containsKey(location)) {
            add(args.get(location), priority, eventArgs);
        } else {
            PriorityHandler<Context> priorityMap = new PriorityHandler<>(new CopyOnWriteArrayList<>(), new ConcurrentHashMap<>());
            add(priorityMap, priority, eventArgs);
            args.put(location, priorityMap);
        }
    }

    public void add(
            PriorityHandler<Context> priorityHandler,
            long priority,
            EventArgs<Context> eventArgs
    ) {
        final var priorities = priorityHandler.priorities;
        final var priorityMap = priorityHandler.priorityMap;
        if (!priorities.contains(priority)) {
            priorities.add(priority);
            priorities.sort(comparator);
        }
        if (priorityMap.containsKey(priority))
            priorityMap.get(priority).add(eventArgs);
        else {
            final Queue<EventArgs<Context>> argsQueue = new ConcurrentLinkedQueue<>();
            argsQueue.add(eventArgs);
            priorityMap.put(priority, argsQueue);
        }
    }


    public void addAll(EventRegistry<Context> eventRegistry) {
        for (final var location : eventRegistry.locations) {
            final var priorityHandler = eventRegistry.args.get(location);
            for (final var priority : priorityHandler.priorities) {
                addAll(location, priority, priorityHandler.priorityMap.get(priority));
            }
        }
    }

    public void addAll(String location, long priority, Queue<EventArgs<Context>> eventArgs) {
        locations.add(location);
        if (args.containsKey(location)) {
            addAll(args.get(location), priority, eventArgs);
        } else {
            PriorityHandler<Context> priorityMap = new PriorityHandler<>(new CopyOnWriteArrayList<>(), new ConcurrentHashMap<>());
            addAll(priorityMap, priority, eventArgs);
            args.put(location, priorityMap);
        }
    }

    public void addAll(
            PriorityHandler<Context> priorityHandler,
            long priority,
            Queue<EventArgs<Context>> eventArgs
    ) {
        final var priorities = priorityHandler.priorities;
        final var priorityMap = priorityHandler.priorityMap;
        if (!priorities.contains(priority)) {
            priorities.add(priority);
            priorities.sort(comparator);
        }
        if (priorityMap.containsKey(priority))
            priorityMap.get(priority).addAll(eventArgs);
        else {
            priorityMap.put(priority, eventArgs);
        }
    }

    public static class PriorityHandler<Context> {
        public final List<Long> priorities;
        public final Map<Long, Queue<EventArgs<Context>>> priorityMap;

        PriorityHandler(List<Long> priorities, Map<Long, Queue<EventArgs<Context>>> priorityMap) {
            this.priorities = priorities;
            this.priorityMap = priorityMap;
        }
    }
}
