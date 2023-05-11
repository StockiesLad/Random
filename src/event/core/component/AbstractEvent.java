package event.core.component;

import event.core.interactive.EventArgs;
import event.core.interactive.StoredArgs;

import java.util.*;
import java.util.function.BiConsumer;

import static event.core.interactive.EventConstants.GENERIC_LOCATION;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

public abstract class AbstractEvent<Context, Event extends AbstractEvent<Context, Event>> implements
        AbstractEventRegistry<Context, Event>,
        AbstractEventExecutor<Context>
{
    private Comparator<Long> eventOrder;

    public AbstractEvent(final Comparator<Long> eventOrder) {
        this.eventOrder = eventOrder;
    }

    protected void setGenericLocation(List<Map<String, StoredArgs<Context, Event>>> argsStorages) {
        for (final var storage : argsStorages)
            storage.put(GENERIC_LOCATION, getNewArgsStorage());
    }

    public void flipOrder() {
        eventOrder = eventOrder.reversed();
    }
    public Comparator<Long> getOrder() {
        return eventOrder;
    }

    /**
     * This is to get a list of locations for which the event will be called. Locations are essentially
     * nested events to create multiple different sub event streams for the same contexts. It's used to
     * save memory and clean up code a bit. Locations should not be used to distinguish between immutable
     * and mutable contexts.
     * @return a list of locations.
     */
    public abstract Queue<String> getAllLocations();

    /**
     * This is used when executing the event to check whether it can even be executed first.
     * @param locations locations to test
     * @return whether the locations exist
     */
    public boolean hasLocation(List<String> locations) {
        for (final var location : locations) {
            if (location != null && getArgsStorage().containsKey(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Single location alternative to {@link #hasLocation(List)}
     * @param location location to test
     * @return whether the location exists
     */
    public boolean hasLocation(String location) {
        return getArgsStorage().containsKey(location);
    }

    /**
     * Checks to see whether this event is empty and has no actions that are currently registered.
     * @return whether there are any active actions.
     */
    public boolean isNotEmpty() {
        for (final var location : getAllLocations())
            if (isNotEmpty(location))
                return true;
        return false;
    }

    /**
     * Checks to see whether this event location is empty and has no actions that are currently registered.
     * @param locations a location (or subbranch) for this event to check.
     * @return whether there are any active actions in this location.
     */
    public boolean isNotEmpty(final String... locations) {
        for (final var location : locations) {
            final var eventHolder = getArgsStorage().get(location);
            if (eventHolder != null) {
                for (final var priority : eventHolder.priorities()) {
                    if (!eventHolder.args().get(priority).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Cleans the event of unused priorities.
     */
    protected void clean(final StoredArgs<Context, Event> eventHolder) {
        if (eventHolder != null) {
            final List<Long> emptyPriorities = new ArrayList<>();
            for (final var priority : eventHolder.priorities()) {
                if (eventHolder.args().get(priority).isEmpty())
                    emptyPriorities.add(priority);
            }
            for (final var priority : emptyPriorities) {
                eventHolder.priorities().remove(priority);
                eventHolder.args().remove(priority);
            }
        }
    }

    /**
     * Cleans the event of unused priorities, locations, lists, maps, etc.
     */
    protected abstract void clean();


    /**
     * Default iteration method for argsStorages.
     * @param argsStorage any storage in the event.
     * @param argsConsumer consumes the args to be handled.
     */
    protected void iterateArgsStorage(StoredArgs<Context, Event> argsStorage,
                                       BiConsumer<Long, EventArgs<Context, Event>> argsConsumer) {
        for (long priority : argsStorage.priorities())
            for (final var  arg : argsStorage.args().get(priority))
                argsConsumer.accept(priority, arg);
    }

    /**
     * @return a new {@link StoredArgs EventHolder}
     */
    protected StoredArgs<Context, Event> getNewArgsStorage() {
        return new StoredArgs<>(synchronizedMap(new HashMap<>()), synchronizedList(new ArrayList<>()));
    }

    /**
     * @return The current {@link StoredArgs args}
     */
    public abstract Map<String, StoredArgs<Context, Event>> getArgsStorage();
}
