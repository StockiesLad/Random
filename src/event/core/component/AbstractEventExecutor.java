package event.core.component;

import java.util.List;

import static event.core.interactive.EventConstants.GENERIC_LOCATION_LIST;

public interface AbstractEventExecutor<Context>{
    /**
     * To set up an event listener, just mixin to your target, call this method with a new event context.
     * @param locations All locations to be executed.
     * @param orderFlipped should event order be flipped when firing?
     * @param eventContext The provided context (of params) through this classes generic.
     */
    void execute(final List<String> locations, final boolean orderFlipped, final Context eventContext);

    /**
     * Single location alternative to {@link #execute(List, boolean, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(String location, final boolean orderFlipped, final Context eventContext) {
        execute(List.of(location), orderFlipped, eventContext);
    }

    /**
     * A default ordered alternative to {@link #execute(List, boolean, Context)}
     * @param locations All locations to be executed.
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(final List<String> locations, final Context eventContext) {
        execute(locations, false, eventContext);
    }

    /**
     * Single location alternative to {@link #execute(List, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(String location, final Context eventContext) {
        execute(List.of(location), eventContext);
    }

    /**
     * Generic location alternative to {@link #execute(List, boolean, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(final boolean orderFlipped, final Context eventContext) {
        execute(GENERIC_LOCATION_LIST, orderFlipped, eventContext);
    }

    /**
     * Generic location and default ordered alternative to {@link #execute(List, boolean, Context)}
     * @param eventContext The provided context (of params) through this classes generic.
     */
    default void execute(final Context eventContext) {
        execute(GENERIC_LOCATION_LIST, false, eventContext);
    }
}
