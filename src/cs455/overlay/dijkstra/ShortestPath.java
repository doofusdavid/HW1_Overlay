package cs455.overlay.dijkstra;

import java.util.ArrayList;

/**
 * Use Dijkstra's algorithm to calculate shortest path
 */
public class ShortestPath
{
    public static int[] ShortestPath(WeightedGraph graph, int node)
    {
        final int[] distance = new int[graph.size()];  // Shortest known distance from 'node'
        final int[] precedingNode = new int[graph.size()]; // Preceeding node in graph
        final boolean[] visited = new boolean[graph.size()];

        for (int i = 0; i < distance.length; i++)
        {
            // Fill up distances with maximum values
            distance[i] = Integer.MAX_VALUE;
        }

        // Distance from node to self is always 0
        distance[node] = 0;

        for (int i = 0; i < distance.length; i++)
        {
            final int next = minimumVertex(distance, visited);
            visited[next] = true;

            // Shortest path to next is distance[next] by way of precedingNode[next]

            final int[] n = graph.neighbors(next);
            for(int j = 0; j<n.length; j++)
            {
                int v = n[j];
                int d = distance[next] + graph.getWeight(next, v);
                if(distance[v] > d)
                {
                    distance[v] = d;
                    precedingNode[v] = next;
                }
            }
        }
        return precedingNode;
    }

    private static int minimumVertex(int[] distance, boolean[] visited)
    {
        int x = Integer.MAX_VALUE;
        int y = -1;

        for (int i = 0; i < distance.length; i++)
        {
            if (!visited[i] && distance[i] < x)
            {
                y = i;
                x = distance[i];
            }
        }
        return y;
    }

    public static void printPath (WeightedGraph graph, int[] precedingNode, int s, int e)
    {
        ArrayList path = new ArrayList();
        int x = e;
        while (x!=s)
        {
            path.add(0, graph.getNode(x));
            x = precedingNode[x];
        }
        path.add(0, graph.getNode(s));
        System.out.println(path);
    }
}
