import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private boolean solvable;
    private Node goal;

    private class Node implements Comparable<Node> {
        private final Board board;
        private final int moves;
        private final Node parent;

        public Node(Board board, int moves, Node parent) {
            this.board = board;
            this.moves = moves;
            this.parent = parent;
        }

        public int compareTo(Node node) {
            if (node == null) throw new IllegalArgumentException();
            return Integer.compare(board.manhattan() + moves, node.board.manhattan() + node.moves);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Board twin = initial.twin();
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> pqtwin = new MinPQ<>();
        pq.insert(new Node(initial, 0, null));
        pqtwin.insert(new Node(twin, 0, null));
        while (true) {
            Node curr = pq.delMin();
            Node curr2 = pqtwin.delMin();
            if (curr.board.isGoal()) {
                solvable = true;
                goal = curr;
                break;
            }
            if (curr2.board.isGoal())
                break;
            for (Board neighbor : curr.board.neighbors()) {
                if (curr.parent == null || !neighbor.equals(curr.parent.board))
                    pq.insert(new Node(neighbor, curr.moves + 1, curr));
            }
            for (Board neighbor : curr2.board.neighbors()) {
                if (curr2.parent == null || !neighbor.equals(curr2.parent.board))
                    pqtwin.insert(new Node(neighbor, curr2.moves + 1, curr2));
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return (solvable) ? goal.moves : -1;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        Stack<Board> stack = new Stack<>();
        Node ptr = goal;
        while (ptr != null) {
            stack.push(ptr.board);
            ptr = ptr.parent;
        }
        return stack;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
