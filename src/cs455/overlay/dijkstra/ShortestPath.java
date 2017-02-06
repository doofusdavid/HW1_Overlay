package cs455.overlay.dijkstra;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Use Dijkstra's algorithm to calculate shortest path
 */
public class ShortestPath
{
    public static ArrayList<NodeDescriptor> ShortestPath(WeightedGraph graph, NodeDescriptor node)
    {
        final int[] distance = new int[graph.size()];  // Shortest known distance from 'node'
        final NodeDescriptor[] precedingNode = new NodeDescriptor[graph.size()]; // Preceeding node in graph
        final boolean[] visited = new boolean[graph.size()];

        for (int i = 0; i < distance.length; i++)
        {
            // Fill up distances with maximum values
            distance[i] = Integer.MAX_VALUE;
        }

        // Distance from node to self is always 0
        distance[graph.indexOfNode(node)] = 0;

        for (int i = 0; i < distance.length; i++)
        {
            final NodeDescriptor next = graph.getNode(minimumVertex(distance, visited));
            visited[graph.indexOfNode(next)] = true;

            // Shortest path to next is distance[next] by way of precedingNode[next]

            final NodeDescriptor[] neighbors = graph.neighbors(next);
            for (int j = 0; j < neighbors.length; j++)
            {
                NodeDescriptor v = neighbors[j];
                int vIndex = graph.indexOfNode(v);
                int d = distance[graph.indexOfNode(next)] + graph.getWeight(next, v);
                if (distance[vIndex] > d)
                {
                    distance[vIndex] = d;
                    precedingNode[vIndex] = next;
                }
            }
        }
        ArrayList<NodeDescriptor> pathList = new ArrayList<>(Arrays.asList(precedingNode));
        return pathList;
    }

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

            final int[] neighbors = graph.neighbors(next);
            for(int j = 0; j<neighbors.length; j++)
            {
                int v = neighbors[j];
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
        System.out.println("Shortest Path from " + s + " to " + e);
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
