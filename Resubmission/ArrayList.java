/**
 * Simple dynamic array with doubling growth.
 * @param <T> element type
 */
public class ArrayList<T> implements List<T> {
    private T[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        elements = (T[]) new Object[10]; // initial capacity
        size = 0;
    }

    /** O(1): return current number of elements */
    public int size() { return size; }

    /** O(1): append to the end */
    public boolean add(T element) {
        grow(size + 1);          // ensure capacity
        elements[size++] = element;
        return true;
    }

    /** O(n): insert at index (shift tail right) */
    public void add(int index, T element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        grow(size + 1);
        for (int i = size; i > index; i--) elements[i] = elements[i - 1];
        elements[index] = element;
        size++;
    }

    /** O(1): read at index */
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[index];
    }

    /** O(n): remove at index (shift tail left) */
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        T out = elements[index];
        for (int i = index; i < size - 1; i++) elements[i] = elements[i + 1];
        elements[--size] = null; // avoid loitering
        return out;
    }

    /** O(n) when growth happens: double capacity when full */
    @SuppressWarnings("unchecked")
    private void grow(int capacity) {
        if (capacity <= elements.length){
            return;
        }
        int newCapacity = Math.max(elements.length * 2, capacity);
        T[] bigger = (T[]) new Object[newCapacity];
        System.arraycopy(elements, 0, bigger, 0, size);
        elements = bigger;
    }
}
