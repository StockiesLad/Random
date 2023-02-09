import java.util.HashMap;
import java.util.Map;

public interface NeoObject<T> {
    Map<NeoObject<?>, Map<String, Object>> FIELD_HOLDER = new HashMap<>();

    NeoObject<T> Constructor(T params);

    default void fieldBuilder(final MapHelper helper) {
        var map = new HashMap<String, Object>();
        helper.addFields(map);
        FIELD_HOLDER.putIfAbsent(this, new HashMap<>());
        map.forEach((s, o) -> FIELD_HOLDER.get(this).put(s, o));
    }

    default Map<String, Object> getFields() {
        final var fields = FIELD_HOLDER.get(this);
        if (fields == null) {
            throw new RuntimeException("NeoObject#getFields was called before fieldBuilder.");
        } else return fields;
    }

    default Object getField(final String fieldName, final String errorMessage) {
        final Object string = getFields().get("string");
        if (string == null) {
            throw new RuntimeException(errorMessage);
        } else return getFields().get(fieldName);
    }

    default Object getField(final String fieldName) {
        return getField(fieldName, fieldName + " was null in: " + getFields());
    }

    interface MapHelper {
        void addFields(final HashMap<String, Object> fieldHolder);
    }

    record Pair(String fieldName, Object fieldValue) {}
}
