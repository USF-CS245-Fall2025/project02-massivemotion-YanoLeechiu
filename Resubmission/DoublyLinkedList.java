/**
 * A doubly linked list with head and tail pointers.
 *
 * @param <T> element type
 */
public class DoublyLinkedList<T> implements List<T> {

    /**
     * Node holding a value and links to the next and previous nodes.
     */
    private static class Node<T> {
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value) {
            this.value = value;
        }
    }

    /** Pointer to the first node (or {@code null} if empty). */
    private Node<T> head;

    /** Pointer to the last node (or {@code null} if empty). */
    private Node<T> tail;

    /** Current number of elements in the list. */
    private int size;

    /**
     * Create an empty doubly linked list.
     */
    public DoublyLinkedList() {
        head = tail = null;
        size = 0;
    }

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
     * Runs in O(1) time.
     *
     * @param element value to append
     * @return always {true}
     */
    public boolean add(T element) {
        Node<T> node = new Node<>(element);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
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
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == size) { // append
            add(element);
            return;
        }

        Node<T> current = nodeAt(index);
        Node<T> node = new Node<>(element);

        node.next = current;
        node.prev = current.prev;

        if (current.prev != null) {
            current.prev.next = node;
        } else {
            head = node;
        }
        current.prev = node;

        size++;
    }

    /**
     * Get the element at the specified index.
     * Runs in O(n) time due to traversal (O(min(i, n-i)) with bidirectional walk).
     *
     * @param index position to read (0..size-1)
     * @return element at {@code index}
     * @throws IndexOutOfBoundsException if index not in [0, size-1]
     */
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return nodeAt(index).value;
    }

    /**
     * Remove and return the element at the specified index.
     * Runs in O(n) time due to traversal (O(min(i, n-i)) with bidirectional walk).
     *
     * @param index position to remove (0..size-1)
     * @return removed element
     * @throws IndexOutOfBoundsException if index not in [0, size-1]
     */
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        Node<T> current = nodeAt(index);

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev;
        }

        size--;
        return current.value;
    }

    /**
     * Return the node at index {@code i}, walking from the nearer end.
     * Runs in O(min(i, n-i)) time.
     *
     * @param i target index
     * @return node at {@code i}
     */
    private Node<T> nodeAt(int i) {
        if (i < size / 2) {
            Node<T> current = head;
            for (int k = 0; k < i; k++) current = current.next;
            return current;
        } else {
            Node<T> current = tail;
            for (int k = size - 1; k > i; k--) current = current.prev;
            return current;
        }
    }
}
