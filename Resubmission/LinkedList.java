/**
 * Singly linked list (head pointer).
 */
public class LinkedList<T> implements List<T> {

    /** Node with value and next pointer */
    private static class Node<T> {
        T value;
        Node<T> next;
        Node(T v) { this.value = v; }
    }

    private Node<T> head; // first node
    private int size;     // element count

    /** Create empty list */
    public LinkedList() { head = null; size = 0; }

    /** O(1) */
    public int size() { return size; }

    /** O(n): append at tail */
    public boolean add(T element) {
        Node<T> node = new Node<>(element);
        if (head == null) {
            head = node;
        } else {
            Node<T> cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = node;
        }
        size++;
        return true;
    }

    /** O(n): insert at index */
    public void add(int index, T element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == 0) {
            Node<T> node = new Node<>(element);
            node.next = head;
            head = node;
        } else {
            Node<T> prev = nodeAt(index - 1);
            Node<T> node = new Node<>(element);
            node.next = prev.next;
            prev.next = node;
        }
        size++;
    }

    /** O(n): read at index */
    public T get(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        return nodeAt(index).value;
    }

    /** O(n): remove at index */
    public T remove(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        T out;
        if (index == 0) {
            out = head.value;
            head = head.next;
        } else {
            Node<T> prev = nodeAt(index - 1);
            Node<T> target = prev.next;
            out = target.value;
            prev.next = target.next;
        }
        size--;
        return out;
    }

    /** O(n): walk to node at i */
    private Node<T> nodeAt(int i) {
        Node<T> cur = head;
        for (int k = 0; k < i; k++){
            cur = cur.next;
        }
        return cur;
    }
}
