import java.util.List;

@FunctionalInterface
public interface Quickerator<T> {

    List<T> getValues();

    default void forEach(final ElementProvider<T> provider) {
        for (T type : getValues()) {
            if (type != null)
                provider.with(type);
        }
    }

    static <T> Quickerator<T> of(final List<T> list) {
        return () -> list;
    }

    @FunctionalInterface
    interface ElementProvider<T> {
        void with(final T element);
    }
}
