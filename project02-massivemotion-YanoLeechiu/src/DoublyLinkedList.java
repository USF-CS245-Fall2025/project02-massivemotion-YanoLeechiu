
// keeps track of head and tail pointers
public class DoublyLinkedList<T> implements List<T> {

    // holds value v, next n, and prev p
    private static class Node<T>{ T v; Node<T> n, p; Node(T v){ this.v = v; } }

    private Node<T> h, t; // head and tail
    private int size;

    public DoublyLinkedList(){ h = t = null; size = 0; }

    public int size(){ return size; }

    // append to tail, O(N)
    public boolean add(T element){
        Node<T> x = new Node<>(element);
        if (h == null){ h = t = x; }
        else { t.n = x; x.p = t; t = x; }
        size++; return true;
    }
    // Insert at index, can go back and forth since Doubly linked list
    public void add(int index, T element){
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == size){ // append
            add(element);
            return;
        }
        Node<T> c = nodeAt(index);
        Node<T> x = new Node<>(element);
        x.n = c; // x.next -> current node
        x.p = c.p; // x.prev -> current node's prev
        if (c.p != null){
            c.p.n = x;
        } else{
            h = x; // insert at head
        }
        c.p = x; // x = current node's prev
        size++;
    }

    // get element at index
    public T get(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return nodeAt(index).v;
    }
    // remove and return removed element at index
    public T remove(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> c = nodeAt(index);
        if (c.p != null) c.p.n = c.n; else h = c.n;
        if (c.n != null) c.n.p = c.p; else t = c.p;
        size--;
        return c.v;
    }

    // Helper to return node at indez
    private Node<T> nodeAt(int i){
        if (i < size/2){
            Node<T> c = h; // start from head
            for (int k=0;k<i;k++) c = c.n;
            return c;
        } else {
            Node<T> c = t; // start from tail
            for (int k=size-1;k>i;k--) c = c.p;
            return c;
        }
    }
}
