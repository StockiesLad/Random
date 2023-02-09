import java.util.List;

public class Main {
    public static void main(String[] args) {
        new Printer(){}.Constructor(
                List.of(new Printer.Pair("string", "Hi, this is an interface pretending to be an object"))
        ).print();
        System.out.println(WomanDefinition.getWoman(true));
    }
}