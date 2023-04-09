import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private HashMap<String, ArrayList<Integer>> map2index;
    private ArrayList<String> map2str;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
        map2index = new HashMap<>();
        map2str = new ArrayList<>();
        In sets = new In(synsets), hypers = new In(hypernyms);

        // read all nouns to hashmap
        while (!sets.isEmpty()) {
            String[] line = sets.readLine().split(",");
            int index = Integer.parseInt(line[0]);
            map2str.add(line[1]);
            for (String word : line[1].split(" ")) {
                if (!map2index.containsKey(word))
                    map2index.put(word, new ArrayList<>());
                map2index.get(word).add(index);
            }
        }

        // read hypernyms to build a digraph
        Digraph digraph = new Digraph(map2str.size());
        while (!hypers.isEmpty()) {
            String[] line = hypers.readLine().split(",");
            int index = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++)
                digraph.addEdge(index, Integer.parseInt(line[i]));
        }

        checkDAG(digraph);
        checkRoot(digraph);

        sap = new SAP(digraph);
    }

    private void checkDAG(Digraph digraph) {
        DirectedCycle check = new DirectedCycle(digraph);
        if (check.hasCycle())
            throw new IllegalArgumentException("The hypersyms cannot form a DAG.");
    }

    // In the topological order of DAG, only the last vertex X could be the answer
    // The out_degree of X is 0
    // Suppose there is an 0-out_degree vertex before X, this vertex can't reach X
    // If all vertices but X has positive out_degree
    // In the topological order, all vertices except X have a path to their latter vertex
    // All vertices have certain path to X, which means DAG has only one root
    private void checkRoot(Digraph digraph) {
        boolean root = false;
        for (int i = 0; i < digraph.V(); i++)
            if (digraph.outdegree(i) == 0)
                if (!root) root = true;
                else throw new IllegalArgumentException("Multiple roots in the hypersyms.");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map2index.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return map2index.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return sap.length(map2index.get(nounA), map2index.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        int index = sap.ancestor(map2index.get(nounA), map2index.get(nounB));
        return map2str.get(index);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        for (String noun : wn.nouns())
            StdOut.println(noun);
    }
}
