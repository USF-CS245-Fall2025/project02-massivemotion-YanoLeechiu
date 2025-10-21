public interface List<T> {

    public void add (int index, T element);
    public boolean add (T element);
    public T get (int index);
    public T remove (int index);
    public int size ();
}
