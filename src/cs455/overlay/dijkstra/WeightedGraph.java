package cs455.overlay.dijkstra;

public class WeightedGraph
{
    private int[][] edges;
    private NodeDescriptor [] nodes;

    public WeightedGraph(int n)
    {
        edges = new int[n][n];
        nodes = new NodeDescriptor[n];
    }

    public int size()
    {
        return nodes.length;
    }

    public void setNode (int vertex, NodeDescriptor node)
    {
        nodes[vertex] = node;
    }
    public NodeDescriptor getNode (int vertex)
    {
        return nodes[vertex];
    }

    public void addEdge (int source, int target, int weight)
    {
        edges[source][target] = weight;
    }

    /**
     * determines if there is an edge between two nodes
     * @param source the source Node ID
     * @param target the target Node ID
     * @return whether there is an edge between them
     */
    public boolean isEdge (int source, int target)
    {
        return edges[source][target] > 0;
    }

    public void removeEdge(int source, int target)
    {
        edges[source][target] = 0;
    }

    public int getWeight(int source, int target)
    {
        return edges[source][target];
    }

    public int[] neighbors (int vertex)
    {
        int count = 0;
        for (int i=0; i<edges[vertex].length; i++)
        {
            if(edges[vertex][i]>0)
                count++;
        }
        final int[]answer = new int[count];
        count = 0;
        for(int i=0;i<edges[vertex].length; i++)
        {
            if(edges[vertex][i]>0)
                answer[count++] = i;
        }
        return answer;
    }

    public void print()
    {
        for(int i=0; i<edges.length; i++)
        {
            System.out.print(nodes[i].toString());
            for(int j=0; j<edges[i].length; j++)
            {
                if(edges[i][j]>0)
                {
                    System.out.print(nodes[j].toString() + " - " + edges[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
