/**
 * Singly linked list that uses a dummy head (sentinel) node.
 * The sentinel simplifies insert/remove at index 0.
 *
 * @param <T> element type
 */
public class DummyHeadLinkedList<T> implements List<T> {

    /**
     * Node holding a value and a pointer to the next node.
     */
    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    /** Sentinel head node (does not store a real value). */
    private final Node<T> head = new Node<>(null);

    /** Current number of elements in the list. */
    private int size = 0;

    /**
     * Return the number of elements in the list.
     * Runs in O(1) time.
     *
     * @return element count
     */
    public int size() {
        return size;
    }

    /**
     * Append an element at the tail of the list.
     * Runs in O(n) time (must walk to the end).
     *
     * @param element value to append
     * @return always {true}
     */
    public boolean add(T element) {
        Node<T> current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = new Node<>(element);
        size++;
        return true;
    }

    /**
     * Insert an element at a specific index, shifting the element
     * currently at that position (and any subsequent elements) to the right.
     * Runs in O(n) time due to traversal.
     *
     * @param index   position at which to insert (0..size)
     * @param element value to insert
     * @throws IndexOutOfBoundsException if index not in [0, size]
     */
    public void add(int index, T element) {
        if (index < 0 || index > size) {
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

    /**
     * Get the element at the specified index.
     * Runs in O(n) time due to traversal.
     *
     * @param index position to read (0..size-1)
     * @return element at {index}
     * @throws IndexOutOfBoundsException if index not in [0, size-1]
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    /**
     * Remove and return the element at the specified index.
     * Runs in O(n) time due to traversal.
     *
     * @param index position to remove (0..size-1)
     * @return removed element
     * @throws IndexOutOfBoundsException if index not in [0, size-1]
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> prev = head;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }
        Node<T> target = prev.next;
        prev.next = target.next;
        size--;
        return target.value;
    }
}
