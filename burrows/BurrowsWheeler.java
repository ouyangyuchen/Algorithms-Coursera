import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.Objects;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String str = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(str);
        // find the index of original string int the sorted circular suffices
        for (int i = 0; i < csa.length(); i++)
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        // output t array
        for (int i = 0; i < csa.length(); i++) {
            int last = (csa.index(i) + csa.length() - 1) % csa.length();
            BinaryStdOut.write(str.charAt(last));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        // key-index count
        int[] cnt = new int[R + 1];
        for (int i = 0; i < t.length(); i++)
            ++cnt[t.charAt(i) + 1];
        for (int i = 1; i < R + 1; i++)
            cnt[i] += cnt[i - 1];
        // construct next array based on cnt
        int[] next = new int[t.length()];
        for (int i = 0; i < t.length(); i++)
            next[cnt[t.charAt(i)]++] = i;
        char[] head = t.toCharArray();
        Arrays.sort(head);
        int ptr = first;
        for (int j = 0; j < t.length(); j++) {
            BinaryStdOut.write(head[ptr]);
            ptr = next[ptr];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (Objects.equals(args[0], "-")) transform();
        else if (Objects.equals(args[0], "+")) inverseTransform();
        else throw new IllegalArgumentException();
    }

}
