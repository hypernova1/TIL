public interface LinkedList<T> {
    void add(T t);
    void add(int index, T t);
    void delete(int index);
    T get(int index);
    boolean isEmpty();
    int size();
}
