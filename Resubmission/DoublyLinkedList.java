/**
 * doublyLinkedListWithHeadAndTailPointers
 */
public class DoublyLinkedList<T> implements List<T> {

    /** nodeWithValueNextAndPrevPointers */
    private static class Node<T> {
        T value;
        Node<T> next, prev;
        Node(T v) { this.value = v; }
    }

    private Node<T> head, tail; // ends
    private int size;

    public DoublyLinkedList() {
        head = tail = null;
        size = 0;
    }

    /** returnSizeO1 */
    public int size() { return size; }

    /** appendAtTailO1 */
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

    /** insertAtIndexOn */
    public void add(int index, T element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == size) { add(element); return; } // append

        Node<T> cur = nodeAt(index);
        Node<T> node = new Node<>(element);
        node.next = cur;
        node.prev = cur.prev;
        if (cur.prev != null) cur.prev.next = node;
        else head = node;
        cur.prev = node;
        size++;
    }

    /** getAtIndexOn */
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return nodeAt(index).value;
    }

    /** removeAtIndexOn */
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> cur = nodeAt(index);
        if (cur.prev != null) cur.prev.next = cur.next; else head = cur.next;
        if (cur.next != null) cur.next.prev = cur.prev; else tail = cur.prev;
        size--;
        return cur.value;
    }

    /** walkFromNearerEndOn
     * usesMinOfIAndNMinusISoMiddleIsOnWorstCaseButEndsAreFaster */
    private Node<T> nodeAt(int i) {
        if (i < size / 2) {
            Node<T> cur = head;
            for (int k = 0; k < i; k++) cur = cur.next;
            return cur;
        } else {
            Node<T> cur = tail;
            for (int k = size - 1; k > i; k--) cur = cur.prev;
            return cur;
        }
    }
}
