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
            System.out.print(nodes[i].toString() + ":");
            for(int j=0; j<edges[i].length; j++)
            {
                if(edges[i][j]>0)
                {
                    System.out.print(nodes[j].Index + " - " + edges[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public static void main (String args[])
    {
        final WeightedGraph t = new WeightedGraph(6);
        t.setNode(0, new NodeDescriptor(0,"v0",100));
        t.setNode(1, new NodeDescriptor(1,"v1",100));
        t.setNode(2, new NodeDescriptor(2,"v2",100));
        t.setNode(3, new NodeDescriptor(3,"v3",100));
        t.setNode(4, new NodeDescriptor(4,"v4",100));
        t.setNode(5, new NodeDescriptor(5,"v5",100));
        t.addEdge(0, 1, 2);
        t.addEdge(0, 5, 9);
        t.addEdge(1, 2, 8);
        t.addEdge(1, 3, 15);
        t.addEdge(1, 5, 6);
        t.addEdge(2, 3, 1);
        t.addEdge(4, 3, 3);
        t.addEdge(4, 2, 7);
        t.addEdge(5, 4, 3);
        t.print();
    }
}
