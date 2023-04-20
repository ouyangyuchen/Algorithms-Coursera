import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    private HashMap<String, Integer> indices;           // team name -> index
    private String[] names;                             // index -> team name
    private int[] wins, losses, remainings;
    private int[][] againsts;
    private final int n;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        indices = new HashMap<>();
        names = new String[n];
        wins = new int[n];
        losses = new int[n];
        remainings = new int[n];
        againsts = new int[n][n];
        for (int i = 0; i < n; i++) {
            String teamname = in.readString();
            indices.put(teamname, i);
            names[i] = teamname;
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remainings[i] = in.readInt();
            for (int j = 0; j < n; j++)
                againsts[i][j] = in.readInt();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return indices.keySet();
    }

    private void validateTeam(String team) {
        if (team == null || !indices.containsKey(team))
            throw new IllegalArgumentException();
    }

    // number of wins for the given team
    public int wins(String team) {
        validateTeam(team);
        return wins[indices.get(team)];
    }

    // number of losses for the given team
    public int losses(String team) {
        validateTeam(team);
        return losses[indices.get(team)];
    }

    // number of remaining games for the given team
    public int remaining(String team) {
        validateTeam(team);
        return remainings[indices.get(team)];
    }

    // number of remaining games between two teams
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return againsts[indices.get(team1)][indices.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        Iterable<String> subset = certificateOfElimination(team);
        return subset != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        ArrayList<String> subset = new ArrayList<>();
        // construct flow network
        int[] temp = new int[n - 1];        // n-2 -> n-1 (without team index)
        int v = 1 + n + (n - 1) * (n - 2) / 2;
        FlowNetwork fn = new FlowNetwork(v);        // s: 0, t: v-1
        int index = indices.get(team);
        int cnt = 0;
        int win_remaining = wins[index] + remainings[index];
        for (int i = 0; i < n; i++)
            if (i != index) temp[cnt++] = i;
        for (int i = 0; i < n - 1; i++) {
            int gameindex = (n - 1) * i - i * (i + 1) / 2;
            if (win_remaining >= wins[temp[i]])
                // connect team vertex to t
                fn.addEdge(new FlowEdge(v - n + i, v - 1, win_remaining - wins[temp[i]]));
            else {
                subset.add(names[temp[i]]);
                return subset;              // trivial case
            }
            for (int j = i + 1; j < n - 1; j++) {
                int cap = againsts[temp[i]][temp[j]];
                // connect game vertex to s
                fn.addEdge(new FlowEdge(0, gameindex + j - i, cap));
                // connect game(i-j) to team vertex i, j
                fn.addEdge(new FlowEdge(gameindex + j - i, v - n + i, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(gameindex + j - i, v - n + j, Double.POSITIVE_INFINITY));
            }
        }
        FordFulkerson ff = new FordFulkerson(fn, 0, v - 1);
        for (int i = 0; i < n - 1; i++)
            if (ff.inCut(v - n + i)) subset.add(names[temp[i]]);
        return (subset.size() > 0) ? subset : null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
