package event.core.component;

import java.util.Comparator;

public abstract class AbstractStaticEvent<Context, Event extends AbstractEvent<Context, Event>>
        extends AbstractEvent<Context, Event>
        implements AbstractStaticEventRegistry<Context, Event>
{
    public AbstractStaticEvent(Comparator<Long> eventOrder) {
        super(eventOrder);
    }
}
