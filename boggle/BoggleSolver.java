import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private static int[] scores = { 1, 2, 3, 5, 11 };

    private class Node {
        Node[] next = new Node[26];
        boolean value;
    }

    private Node root;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String word : dictionary)
            insert(root, word, 0);
    }

    // Search word in the trie, return the node that possibly contains the value
    private Node search(Node x, String word, int d) {
        if (x == null) return null;
        if (d == word.length()) return x;
        char c = word.charAt(d);
        return search(x.next[c - 'A'], word, d + 1);
    }

    // Insert word to trie
    private Node insert(Node x, String word, int d) {
        if (x == null) x = new Node();
        if (d == word.length()) {
            x.value = true;
            return x;
        }
        char c = word.charAt(d);
        x.next[c - 'A'] = insert(x.next[c - 'A'], word, d + 1);
        return x;
    }

    // Whether trie contains word
    private boolean contains(String word) {
        Node tmp = search(root, word, 0);
        return tmp != null && tmp.value;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();
        Set<String> res = new HashSet<>();
        int m = board.rows(), n = board.cols();
        boolean[][] onpath = new boolean[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                dfs(board, i, j, "", res, onpath, root);
        return res;
    }

    // depth-first-search on the board, while recording the current Node in the trie
    private void dfs(BoggleBoard board, int i, int j, String prefix, Set<String> res,
                     boolean[][] onpath, Node ptr) {
        char c = board.getLetter(i, j);
        prefix = prefix + c;
        ptr = ptr.next[c - 'A'];
        if (c == 'Q' && ptr != null) {
            ptr = ptr.next['U' - 'A'];
            prefix = prefix + 'U';
        }

        if (ptr == null) return;        // no words matched in the trie
        onpath[i][j] = true;
        if (ptr.value && prefix.length() >= 3)
            res.add(prefix);            // valid word

        for (int neighbor : neighbors(i, j, board.rows(), board.cols())) {
            int x = neighbor / board.cols(), y = neighbor % board.cols();
            if (!onpath[x][y])          // avoid cycle
                dfs(board, x, y, prefix, res, onpath, ptr);
        }
        onpath[i][j] = false;           // backtrack
    }

    // return the neighbors of (i, j) in the board
    private Iterable<Integer> neighbors(int i, int j, int m, int n) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int dx = -1; dx < 2; dx++)
            for (int dy = -1; dy < 2; dy++) {
                if (dx == 0 && dy == 0) continue;
                int x = dx + i, y = dy + j;
                if (x >= 0 && x < m && y >= 0 && y < n)
                    res.add(n * x + y);
            }
        return res;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();
        if (!contains(word)) return 0;
        int len = word.length();
        if (len <= 2) return 0;
        if (Math.max(len, 4) == 4) return 1;
        if (Math.min(len, 8) == 8) return 11;
        return scores[len - 4];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.print(word);
            int s = solver.scoreOf(word);
            StdOut.println(", score=" + s);
            score += s;
        }
        StdOut.println("Score = " + score);
    }
}
