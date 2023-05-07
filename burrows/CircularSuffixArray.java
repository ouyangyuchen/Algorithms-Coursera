import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private int n;
    private String word;
    private CircularSuffix[] cs;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private int start;

        public CircularSuffix(int start) {
            this.start = start;
        }

        public int compareTo(CircularSuffix obj) {
            for (int i = 0; i < n; i++) {
                int j = (start + i) % n;
                int k = (obj.start + i) % n;
                int res = Character.compare(word.charAt(j), word.charAt(k));
                if (res > 0) return 1;
                else if (res < 0) return -1;
            }
            return 0;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("");
        word = s;
        n = s.length();
        cs = new CircularSuffix[n];
        for (int i = 0; i < n; i++)
            cs[i] = new CircularSuffix(i);
        Arrays.sort(cs);
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException("");
        return cs[i].start;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String test = args[0];
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(test);
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            int temp = circularSuffixArray.index(i);
            StdOut.print(test.substring(i) + test.substring(0, i) + "\t");
            StdOut.print(test.substring(temp) + test.substring(0, temp) + '\t');
            StdOut.println(temp);
        }
    }

}
