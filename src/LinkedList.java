// Singly linked list
public class LinkedList<T> implements List<T> {

    // Node that holds a value (v) and pointer to next (n)
    private static class Node<T> {
        T v;
        Node<T> n;
        Node(T v){ this.v = v; }
    }

    private Node<T> head;
    private int size;

    // Create empty List
    public LinkedList(){
        head = null;
        size = 0;
    }

    public int size(){ return size; }

    // Append/add element to the end of the list
    public boolean add(T element){
        Node<T> x = new Node<>(element);
        if (head == null) {
            // empty list: new node becomes the head
            head = x;
        } else {
            // non-empty: walk to the last node
            Node<T> c = head;
            while (c.n != null) c = c.n;   // stop at tail
            c.n = x;                       // link new node at the end
        }
        size++;
        return true;
    }

    // Insert element at a specific index
    public void add(int index, T element){
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == 0){
            // insert at head: new node points to current head, then becomes head
            Node<T> x = new Node<>(element);
            x.n = head;
            head = x;
        } else {
            // insert after the (index-1)-th node
            Node<T> p = nodeAt(index - 1); // previous node
            Node<T> x = new Node<>(element);
            x.n = p.n;
            p.n = x;
        }
        size++;
    }

    public T get(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return nodeAt(index).v;
    }

    // Remove and return element at position index in [0..size-1]
    // Time: O(n) to find previous node (or O(1) when removing head)
    public T remove(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        T val;
        if (index == 0){
            // remove head: move head to next
            val = head.v;
            head = head.n;
        } else {
            // remove after the (index-1)-th node
            Node<T> p = nodeAt(index - 1);
            Node<T> t = p.n;
            val = t.v;
            p.n = t.n;
        }
        size--;
        return val;
    }

    // Node with next pointer helper
    private Node<T> nodeAt(int i){
        Node<T> c = head;
        for (int k = 0; k < i; k++) c = c.n;
        return c;
    }
}
