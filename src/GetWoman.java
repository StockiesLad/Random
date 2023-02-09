public class GetWoman {
    /**
     * Notice how if you print a woke definition, not even a computer can understand.
     * It's because stupid woke definition is recursive and doesn't make sense.
     * "A woman is someone that identifies as a woman".
     * @param biological If the definition should be biological or woke.
     * @return The definition.
     */
    public static String getWoman(final boolean biological) {
        final String start = "A woman is a";
        if (biological) {
            return start + "n adult human female";
        } else {
            return start + " person that identifies as a " + getWoman(false);
        }
    }
}
