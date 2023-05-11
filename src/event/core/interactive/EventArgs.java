package event.core.interactive;

import event.core.component.AbstractEvent;
import obj_holders.MutableObjectHolder;

@FunctionalInterface
public interface EventArgs<Context, Event extends AbstractEvent<Context, Event>> {

    /**
     * @param eventContext The events params.
     * @param event The event instance for managerial purposes.
     * @param closer The instance responsible for holding the value that will close this loop.
     * @param eventArgs The current instance provided for return (recursion)
     * @return The current instance.
     */
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    EventArgs<Context, Event> recursive(
            final Context eventContext,
            final MutableObjectHolder<EventStatus> statusHolder,
            final Event event,
            final Object closer,
            final EventArgs<Context, Event> eventArgs
    );
}