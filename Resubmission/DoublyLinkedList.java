/**
 * Doubly linked list with head and tail pointers.
 */
public class DoublyLinkedList<T> implements List<T> {

    /** Node with value, next, and prev pointers */
    private static class Node<T>{ T value; Node<T> next, prev; Node(T v){
        this.value = v;
       }
    }

    private Node<T> head, tail; // ends
    private int size;

    public DoublyLinkedList(){
        head = tail = null;
        size = 0;
    }

    /** O(1) */
    public int size(){
        return size;
    }

    /** O(1): append at tail */
    public boolean add(T element){
        Node<T> node = new Node<>(element);
        if (head == null){
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
        return true;
    }

    /** O(n): insert at index */
    public void add(int index, T element){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException();
        }
        if (index == size){
            add(element); // append
            return;
        }

        Node<T> cur = nodeAt(index);
        Node<T> node = new Node<>(element);
        node.next = cur;
        node.prev = cur.prev;
        if (cur.prev != null){
            cur.prev.next = node;
        } else{
            head = node;
        }
        cur.prev = node;
        size++;
    }

    /** O(n): read at index */
    public T get(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        return nodeAt(index).value;
    }

    /** O(n): remove at index */
    public T remove(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> cur = nodeAt(index);
        if (cur.prev != null){
            cur.prev.next = cur.next;
        } else head = cur.next;

        if (cur.next != null){
            cur.next.prev = cur.prev;
        } else tail = cur.prev;
        size--;
        return cur.value;
    }

    /** O(n): walk from nearer end
     * makes everything O(n/2) because you can start at the closer end, so
     * you don't necessarily have to go through the whole array, but that rounds up to O(n) */
    private Node<T> nodeAt(int i){
        if (i < size / 2){
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
