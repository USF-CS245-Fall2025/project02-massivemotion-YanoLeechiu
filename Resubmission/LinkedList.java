/**
 * Singly linked list backed by a head pointer.
 *
 * @param <T> element type
 */
public class LinkedList<T> implements List<T> {

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

    /** First node in the list (null when empty). */
    private Node<T> head;

    /** Current number of elements in the list. */
    private int size;

    /**
     * Create an empty list.
     */
    public LinkedList() {
        head = null;
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
     * Runs in O(n) time (walk to the end).
     *
     * @param element value to append
     * @return always {true}
     */
    public boolean add(T element) {
        Node<T> node = new Node<>(element);
        if (head == null) {
            head = node;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }
        size++;
        return true;
    }

    /**
     * Insert an element at a specific index. Elements at and after the index
     * are shifted one position to the right.
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
        if (index == 0) {
            Node<T> node = new Node<>(element);
            node.next = head;
            head = node;
        } else {
            Node<T> previous = nodeAt(index - 1);
            Node<T> node = new Node<>(element);
            node.next = previous.next;
            previous.next = node;
        }
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
        return nodeAt(index).value;
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
        T out;
        if (index == 0) {
            out = head.value;
            head = head.next;
        } else {
            Node<T> previous = nodeAt(index - 1);
            Node<T> target = previous.next;
            out = target.value;
            previous.next = target.next;
        }
        size--;
        return out;
    }

    /**
     * Return the node at the given index by walking forward from the head.
     * Runs in O(n) time.
     *
     * @param index node position (0..size-1)
     * @return node at {index}
     */
    private Node<T> nodeAt(int index) {
        Node<T> current = head;
        for (int k = 0; k < index; k++) {
            current = current.next;
        }
        return current;
    }
}
