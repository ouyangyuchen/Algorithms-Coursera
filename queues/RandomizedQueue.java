import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] elem;
    private int size, capacity;

    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = 1;
        elem = (Item[]) new Object[capacity];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == capacity) resize(2 * capacity);
        elem[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(size);
        Item temp = elem[index];
        elem[index] = elem[--size];
        if ((double) size / capacity <= 0.25) resize(Math.max(1, capacity / 2));
        return temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(size);
        return elem[index];
    }

    private void resize(int newcapacity) {
        if (capacity == newcapacity) return;
        Item[] temp = (Item[]) new Object[newcapacity];
        for (int i = 0; i < size; i++)
            temp[i] = elem[i];
        elem = temp;
        // StdOut.printf("Capacity changes from %s to %s\n", capacity, newcapacity);
        capacity = newcapacity;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Item> {
        private int[] randindex;
        private int curr;

        public MyIterator() {
            randindex = new int[size];
            for (int i = 0; i < size; i++)
                randindex[i] = i;
            StdRandom.shuffle(randindex);
            curr = 0;
        }

        public boolean hasNext() {
            return curr < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return elem[randindex[curr++]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 6;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
        for (int i = 0; i < n; i++)
            StdOut.println(queue.dequeue());
    }

}
