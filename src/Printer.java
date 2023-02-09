import java.util.List;

public interface Printer extends NeoObject<List<NeoObject.Pair>> {

    @Override
    default Printer Constructor(final List<Pair> fields) {
        fieldBuilder(fieldHolder ->
                Quickerator.of(fields).forEach(element ->
                        fieldHolder.put(element.fieldName(), element.fieldValue()))
        );
        return this;
    }

    default void print() {
        System.out.println((String)getField("string"));
    }

}
