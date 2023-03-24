import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size, capacity;
    // start: the first item index, end: the last item index + 1
    private int start, end;
    private Item[] elem;

    // construct an empty deque
    public Deque() {
        capacity = 1;
        elem = (Item[]) new Object[capacity];
        size = 0;
        start = 0;
        end = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == capacity) resize(2 * capacity);
        start = nextIndex(start, false);
        elem[start] = item;
        ++size;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == capacity) resize(2 * capacity);
        elem[end] = item;
        ++size;
        end = nextIndex(end, true);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item res = elem[start];
        start = nextIndex(start, true);
        --size;
        if ((double) size / capacity <= 0.25) resize(Math.max(1, capacity / 2));
        return res;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        end = nextIndex(end, false);
        Item res = elem[end];
        --size;
        if ((double) size / capacity <= 0.25) resize(Math.max(1, capacity / 2));
        return res;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Item> {
        private int ptr = 0;

        public boolean hasNext() {
            return ptr < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (ptr == size) throw new NoSuchElementException();
            Item res = elem[(start + ptr) % capacity];
            ++ptr;
            return res;
        }
    }

    private int nextIndex(int curr, boolean forward) {
        int step = (forward) ? 1 : -1;
        return (curr + step + capacity) % capacity;
    }

    private void resize(int newcapacity) {
        if (capacity == newcapacity) return;
        Item[] temp = (Item[]) new Object[newcapacity];
        int i = 0;
        for (Item item : this) {
            temp[i++] = item;
        }
        elem = temp;
        // StdOut.printf("Capacity changes from %s to %s\n", capacity, newcapacity);
        capacity = newcapacity;
        start = 0;
        end = size;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        for (int i = 0; i < 10; i++) {
            if (StdRandom.bernoulli())
                deque.addLast(i);
            else
                deque.addFirst(i);
            StdOut.printf(" size [%s]\n", deque.size());
        }
        while (!deque.isEmpty()) {
            int num;
            if (StdRandom.bernoulli())
                num = deque.removeLast();
            else
                num = deque.removeFirst();
            StdOut.printf("Remove %s, size [%s]\n", num, deque.size());
        }
    }
}
