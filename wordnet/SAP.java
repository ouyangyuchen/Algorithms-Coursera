import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {
    private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ArrayList<Integer> arr1 = new ArrayList<>(), arr2 = new ArrayList<>();
        arr1.add(v);
        arr2.add(w);
        return length(arr1, arr2);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ArrayList<Integer> arr1 = new ArrayList<>(), arr2 = new ArrayList<>();
        arr1.add(v);
        arr2.add(w);
        return ancestor(arr1, arr2);
    }

    // return the common ancestor and shortest common ancestral path length
    private int[] path(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(digraph, w);
        int length = Integer.MAX_VALUE;
        int resv = -1;
        for (int i = 0; i < digraph.V(); i++)
            if (bfs_v.hasPathTo(i) && bfs_w.hasPathTo(i)) {
                int currlen = bfs_v.distTo(i) + bfs_w.distTo(i);
                if (currlen < length) {
                    length = currlen;
                    resv = i;
                }
            }
        if (resv == -1) length = -1;
        return new int[] { resv, length };
    }

    private void checkIterable(Iterable<Integer> iter) {
        if (iter == null) throw new IllegalArgumentException("Iterator is null.");
        for (Integer v : iter) {
            if (v == null) throw new IllegalArgumentException("Iterator contains null element.");
            checkValidVertex(v);
        }
    }

    private void checkValidVertex(int v) {
        if (v < 0 || v >= digraph.V())
            throw new IllegalArgumentException(
                    String.format("Vertex %s is out of range of %s.", v, digraph.V()));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;
        int[] res = path(v, w);
        return res[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;
        int[] res = path(v, w);
        return res[0];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
