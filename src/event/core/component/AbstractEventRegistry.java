package event.core.component;

import event.core.interactive.EventArgs;
import event.core.interactive.StableEventArgs;

import java.util.List;

import static event.core.interactive.EventConstants.GENERIC_LOCATION_LIST;

public interface AbstractEventRegistry<Context, Event extends AbstractEvent<Context, Event>>{
    /**
     * Thread unsafe method of removing events. It's faster but will cause CME's if you aren't careful.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be removed from.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    void removeListener(final long priority, final List<String> locations, final EventArgs<Context, Event> eventArgs);

    /**
     * Single location alternative to {@link #removeListener(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void removeListener(final long priority, final String location, final EventArgs<Context, Event> eventArgs) {
        removeListener(priority, List.of(location), eventArgs);
    }

    /**
     * Generic location alternative to {@link #removeListener(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executed.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void removeListener(final long priority, final EventArgs<Context, Event> eventArgs) {
        removeListener(priority, GENERIC_LOCATION_LIST, eventArgs);
    }



    /**
     * Registers a lambda to a map. It's not thread safe so do it at startup to avoid CME's.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    void registerListener(final long priority, final List<String> locations, final EventArgs<Context, Event> eventArgs);

    /**
     * Single location alternative to {@link #registerListener(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerListener(final long priority, final String location, final EventArgs<Context, Event> eventArgs) {
        registerListener(priority, List.of(location), eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link #registerListener(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerListener(final long priority, final List<String> locations,
                                  final StableEventArgs<Context, Event> eventArgs) {
        registerListener(priority, locations, eventArgs.upcast());
    }

    /**
     * Single location alternative to {@link #registerListener(long, List, StableEventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerListener(final long priority, final String location,
                                  final StableEventArgs<Context, Event> eventArgs) {
        registerListener(priority, List.of(location), eventArgs);
    }

    /**
     * Generic location alternative to {@link #registerListener(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerListener(final long priority, final EventArgs<Context, Event> eventArgs) {
        registerListener(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link #registerListener(long, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerListener(final long priority, final StableEventArgs<Context, Event> eventArgs) {
        registerListener(priority, eventArgs.upcast());
    }
}
