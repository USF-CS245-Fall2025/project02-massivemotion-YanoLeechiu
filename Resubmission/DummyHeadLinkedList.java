/**
 * Singly linked list with a dummy head (sentinel) node.
 * Simplifies insert/remove at index 0.
 */
public class DummyHeadLinkedList<T> implements List<T> {

    /** Node with value and next pointer */
    private static class Node<T>{
        T value;
        Node<T> next;
        Node(T v){
            this.value = v;
        }
    }

    private final Node<T> head = new Node<>(null); // sentinel (no real value)
    private int size = 0;

    /** O(1) */
    public int size(){
        return size;
    }

    /** O(n): append at tail */
    public boolean add(T element){
        Node<T> cur = head;
        while (cur.next != null){
            cur = cur.next;
        }
        cur.next = new Node<>(element);
        size++;
        return true;
    }

    /** O(n): insert at index (walk from dummy head) */
    public void add(int index, T element){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException();
        }
        Node<T> prev = head;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }
        Node<T> node = new Node<>(element);
        node.next = prev.next;
        prev.next = node;
        size++;
    }

    /** O(n): read at index */
    public T get(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        Node<T> cur = head.next;

        for (int i = 0; i < index; i++){
            cur = cur.next;
        }
        return cur.value;
    }

    /** O(n): remove at index (walk from dummy head) */
    public T remove(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        Node<T> prev = head;
        for (int i = 0; i < index; i++){
            prev = prev.next;
        }
        Node<T> target = prev.next;
        prev.next = target.next;
        size--;
        return target.value;
    }
}
