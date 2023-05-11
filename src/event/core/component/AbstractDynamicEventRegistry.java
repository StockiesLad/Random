package event.core.component;

import event.core.interactive.EventArgs;
import event.core.interactive.StableEventArgs;

import java.util.List;

import static event.core.interactive.EventConstants.GENERIC_LOCATION_LIST;

public interface AbstractDynamicEventRegistry<Context, Event extends AbstractEvent<Context, Event>>{

    /**
     * Thread safe method to removeListener events. Typically used inside of events to removeListener after execute.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be removed from.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    void removeDynamic(final long priority, final List<String> locations, final EventArgs<Context, Event> eventArgs);

    /**
     * Single location alternative to {@link #removeDynamic(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void removeDynamic(final long priority, final String location, final EventArgs<Context, Event> eventArgs) {
        removeDynamic(priority, List.of(location), eventArgs);
    }

    /**
     * Generic location alternative to {@link #removeDynamic(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void removeDynamic(final long priority, final EventArgs<Context, Event> eventArgs) {
        removeDynamic(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * A thread safe registration method at the expense of speed & efficiency.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    void registerDynamic(final long priority, final List<String> locations, final EventArgs<Context, Event> eventArgs);

    /**
     * Single location alternative to {@link #registerDynamic(long, List, EventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerDynamic(final long priority, final String location, final EventArgs<Context, Event> eventArgs) {
        registerDynamic(priority, List.of(location), eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link #registerDynamic(long, List, EventArgs)}.
     * @param eventArgs The args that will be called when event executes.
     * @param locations All locations that this eventArg should be registered to.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerDynamic(final long priority, final List<String> locations,
                                 final StableEventArgs<Context, Event> eventArgs) {
        registerDynamic(priority, locations, eventArgs.upcast());
    }

    /**
     * Single location alternative to {@link #registerDynamic(long, List, StableEventArgs)}
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerDynamic(final long priority, final String location,
                                 final StableEventArgs<Context, Event> eventArgs) {
        registerDynamic(priority, List.of(location), eventArgs);
    }

    /**
     * Generic location alternative to {@link #registerDynamic(long, List, EventArgs)}.
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerDynamic(final long priority, final EventArgs<Context, Event> eventArgs) {
        registerDynamic(priority, GENERIC_LOCATION_LIST, eventArgs);
    }

    /**
     * StableEventArgs alternative to {@link #registerDynamic(long, EventArgs)}.
     * @param eventArgs The args that will be called when event executes.
     * @param priority Holds the value for the priority value, which correlates with when it's executed.
     */
    default void registerDynamic(final long priority, final StableEventArgs<Context, Event> eventArgs) {
        registerDynamic(priority, eventArgs.upcast());
    }
}
