import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;
import java.util.Objects;

public class MoveToFront {
    private static final int R = 256;

    private static LinkedList<Character> construct() {
        LinkedList<Character> res = new LinkedList<>();
        for (int i = 0; i < R; i++)
            res.add((char) i);
        return res;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> seq = construct();
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            int index = seq.indexOf(ch);
            BinaryStdOut.write(index, 8);
            seq.remove(index);
            seq.add(0, ch);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> seq = construct();
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);
            char ch = seq.get(index);
            BinaryStdOut.write(ch);
            seq.remove(index);
            seq.add(0, ch);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (Objects.equals(args[0], "-")) encode();
        else if (Objects.equals(args[0], "+")) decode();
        else throw new IllegalArgumentException("");
    }

}
