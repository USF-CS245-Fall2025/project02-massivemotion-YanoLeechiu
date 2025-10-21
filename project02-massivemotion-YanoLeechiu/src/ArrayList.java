public class ArrayList<T> implements List<T> {
    private T[] arr;   // backing array
    private int size;  // number of elements currently stored

    @SuppressWarnings("unchecked")
    public ArrayList(){
        //Initian capacity of 10
        arr = (T[]) new Object[10];
        size = 0;
    }

    // O(1): return current number of elements
    public int size(){ return size; }

    // Amortized O(1): append element at the end
    public boolean add(T element){
        ensure(size + 1);          // grow array if needed
        arr[size++] = element;     // store element then bump size
        return true;
    }

    // O(n): insert element at a given index, shifting tail right
    public void add(int index, T element){
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        ensure(size + 1);                         // make room if needed
        // shift elements [index .. size-1] one step right
        for (int i = size; i > index; i--) arr[i] = arr[i-1];
        arr[index] = element;                     // drop new element in place
        size++;                                   // reflect insertion
    }

    // O(1): read element at index
    public T get(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return arr[index];
    }

    // O(n): remove and return element at index, shifting tail left
    public T remove(int index){
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        T val = arr[index];                       // value to return
        // shift elements [index+1 .. size-1] one step left
        for (int i = index; i < size - 1; i++) arr[i] = arr[i+1];
        arr[--size] = null;                       // drop last slot (avoid memory leak)
        return val;
    }

    // O(n) when growth happens: ensure backing array has at least 'cap' capacity
    @SuppressWarnings("unchecked")
    private void ensure(int cap){
        if (cap <= arr.length) return;            // already enough room
        int n = Math.max(arr.length * 2, cap);    // double capacity (or meet required cap)
        T[] na = (T[]) new Object[n];             // allocate new bigger array
        System.arraycopy(arr, 0, na, 0, size);    // copy existing elements
        arr = na;                                 // switch backing array
    }
}
