public class TreeChildrenExtractor<T> implements Comparable<T> {

    private T comparator;

    public TreeChildrenExtractor(T comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compareTo(T object) {
        if (object == null)
            return 1;
        boolean ok = object.equals(comparator);
        return ok ? 0 : 1;
    }

}
