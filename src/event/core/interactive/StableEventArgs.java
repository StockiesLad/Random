package event.core.interactive;

import event.core.component.AbstractEvent;
import obj_holders.MutableObjectHolder;

@FunctionalInterface
public interface StableEventArgs<Context, Event extends AbstractEvent<Context, Event>>
        extends EventArgs<Context, Event> {

    default EventArgs<Context, Event> upcast() {
        return this;
    }

    /**
     * @param eventContext The events params.
     * @param event The event instance for managerial purposes.
     * @param closer The instance responsible for holding the value that will close this loop.
     * @param eventArgs The current instance provided for return (recursion)
     * @return The current instance.
     */
    @Override
    default EventArgs<Context, Event> recursive(
            final Context eventContext,
            final MutableObjectHolder<EventStatus> eventStatus,
            final Event event,
            final Object closer,
            final EventArgs<Context, Event> eventArgs
    ) {
        stable(eventContext, eventStatus, event, eventArgs);
        return null;
    }
    /**
     * @param eventContext The events params.
     * @param eventArgs The current instance provided for return (recursion)
     */
    void stable(
            final Context eventContext,
            final MutableObjectHolder<EventStatus> eventStatus,
            final Event event,
            final EventArgs<Context, Event> eventArgs
    );
}