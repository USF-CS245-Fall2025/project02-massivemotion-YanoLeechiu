/**
 * simpleDynamicArrayWithDoublingGrowth
 * @param <T> elementType
 */
public class ArrayList<T> implements List<T> {
    private T[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        elements = (T[]) new Object[10]; // initialCapacity
        size = 0;
    }

    /** returnCurrentNumberOfElements (O(1)) */
    public int size() { return size; }

    /** appendToEnd (amortized O(1)) */
    public boolean add(T element) {
        grow(size + 1);          // ensureCapacity
        elements[size++] = element;
        return true;
    }

    /** insertAtIndexShiftTailRight (O(n)) */
    public void add(int index, T element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        grow(size + 1);
        for (int i = size; i > index; i--) elements[i] = elements[i - 1];
        elements[index] = element;
        size++;
    }

    /** getAtIndex (O(1)) */
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[index];
    }

    /** removeAtIndexShiftTailLeft (O(n)) */
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        T out = elements[index];
        for (int i = index; i < size - 1; i++) elements[i] = elements[i + 1];
        elements[--size] = null; // avoidLoitering
        return out;
    }

    /** growCapacityByDoublingWhenFull (O(n) when resize happens) */
    @SuppressWarnings("unchecked")
    private void grow(int capacity) {
        if (capacity <= elements.length) {
            return;
        }
        int newCapacity = Math.max(elements.length * 2, capacity);
        T[] bigger = (T[]) new Object[newCapacity];
        System.arraycopy(elements, 0, bigger, 0, size);
        elements = bigger;
    }
}
