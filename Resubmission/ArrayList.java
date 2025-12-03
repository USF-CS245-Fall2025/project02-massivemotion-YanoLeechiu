/**
 * Simple dynamic array with doubling growth.
 * @param <T> element type
 */
public class ArrayList<T> implements List<T> {
    /**
     * Backing array that stores the elements.
     */
    private T[] elements;

    /**
     * Current number of stored elements.
     */
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayList() {
        // Initial capacity is 10
        elements = (T[]) new Object[10];
        size = 0;
    }

    /**
     * Return the current number of elements (O(1)).
     */
    public int size() {
        return size;
    }

    /**
     * Append an element to the end (amortized O(1)).
     */
    public boolean add(T element) {
        grow(size + 1);          // Ensure capacity before writing
        elements[size++] = element;
        return true;
    }

    /**
     * Insert an element at the given index, shifting the tail right (O(n)).
     */
    public void add(int index, T element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        grow(size + 1);                          // Ensure capacity
        for (int i = size; i > index; i--) {     // Shift right
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    /**
     * Read and return the element at the given index (O(1)).
     */
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[index];
    }

    /**
     * Remove and return the element at the given index, shifting the tail left (O(n)).
     */
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        T out = elements[index];
        for (int i = index; i < size - 1; i++) { // Shift left
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;                 // Avoid loitering
        return out;
    }

    /**
     * Ensure capacity by doubling when full (O(n) only when resize happens).
     * If the requested capacity exceeds the next double, grow to the requested capacity.
     */
    @SuppressWarnings("unchecked")
    private void grow(int requiredCapacity) {
        if (requiredCapacity <= elements.length) {
            return;
        }
        int newCapacity = elements.length * 2;
        while (newCapacity < requiredCapacity) {
            newCapacity *= 2;
        }
        T[] bigger = (T[]) new Object[newCapacity];
        System.arraycopy(elements, 0, bigger, 0, size);
        elements = bigger;
    }
}

