/**
 * singlyLinkedListHeadPointer
 */
public class LinkedList<T> implements List<T> {

    /** nodeWithValueAndNextPointer */
    private static class Node<T> {
        T value;
        Node<T> next;
        Node(T v) { this.value = v; }
    }

    private Node<T> head; // firstNode
    private int size;     // elementCount

    /** createEmptyList */
    public LinkedList() { head = null; size = 0; }

    /** returnSizeO1 */
    public int size() { return size; }

    /** appendAtTailOn */
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

    /** insertAtIndexOn */
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

    /** readAtIndexOn */
    public T get(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        return nodeAt(index).value;
    }

    /** removeAtIndexOn */
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

    /** walkToNodeAtIndexOn */
    private Node<T> nodeAt(int i) {
        Node<T> cur = head;
        for (int k = 0; k < i; k++){
            cur = cur.next;
        }
        return cur;
    }
}
