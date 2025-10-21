public class DummyHeadLinkedList<T> implements List<T> {

    private static class Node<T>{ T v; Node<T> n; Node(T v){ this.v = v; } }

    // The sentinel 'h' doesnt holds a real value, just  it removes at index 0.
    // Node structure: value (v) + next pointer (n).
    private final Node<T> h = new Node<>(null);
    private int size = 0;

    // num of elems
    public int size(){ return size; }

    // append elements at the end of list
    public boolean add(T element){
        Node<T> c = h;
        while (c.n != null) c = c.n;
        c.n = new Node<>(element);
        size++; return true;
    }
    // Inserts element at 'index' and "walks" to (index-1)).
    public void add(int index, T element){
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        Node<T> p = h;
        for (int i=0;i<index;i++) p = p.n;
        Node<T> x = new Node<>(element);
        x.n = p.n; p.n = x; size++;
    }
    // Returns element at 'index' in and walks to index
    public T get(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> c = h.n;
        for (int i=0;i<index;i++) c = c.n;
        return c.v;
    }
    // Removes and returns element at 'index'
    public T remove(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> p = h;
        for (int i=0;i<index;i++) p = p.n;
        Node<T> t = p.n; p.n = t.n; size--; return t.v;
    }
}
