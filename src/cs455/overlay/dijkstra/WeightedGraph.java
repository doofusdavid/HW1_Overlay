package cs455.overlay.dijkstra;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class WeightedGraph
{
    private int[][] edges;
    private NodeDescriptor [] nodes;

    public WeightedGraph(int n)
    {
        edges = new int[n][n];
        nodes = new NodeDescriptor[n];
    }

    public WeightedGraph(ArrayList<NodeDescriptor> nodeList, int connectionCount)
    {
        // allocate the edges and nodes
        edges = new int[nodeList.size()][nodeList.size()];
        nodes = new NodeDescriptor[nodeList.size()];

        for (int i = 0; i < nodeList.size(); i++)
        {
            nodes[i] = nodeList.get(i);
            // we had been ignoring indexes until now, fill them in.
            nodes[i].Index = i;
        }

        // Create a "deck" of numbers we'll shuffle to get random node numbers
        ArrayList<Integer> deck = new ArrayList<Integer>();
        for (int i = 0; i < nodeList.size(); i++)
        {
            deck.add(i);
        }

        // keep track of connection counts per node, no more than 4
        int[] cCount = new int[nodeList.size()];

        for (NodeDescriptor node : nodes)
        {
            Collections.shuffle(deck);
            Random random = new Random();

            for(int i = 0; i < nodeList.size(); i++)
            {
                if(cCount[node.Index] < connectionCount)
                {
                    int nodeToLink = deck.get(cCount[node.Index]);

                    // skip if it would link to itself, or the node we're
                    // linking to already has enough connections
                    if ((nodeToLink == node.Index) ||
                            (cCount[nodeToLink] >= connectionCount))
                    {
                        continue;
                    } else
                    {
                        // add an edge and increment the count of connections
                        this.addEdge(node.Index, nodeToLink, random.nextInt(10) + 1);
                        cCount[node.Index]++;
                        cCount[nodeToLink]++;
                    }
                }
            }
        }
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
        ArrayList<NodeDescriptor> nodeList = new ArrayList<>();
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9000));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9001));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9002));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9003));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9004));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9005));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9006));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9007));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9008));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9009));
        nodeList.add(new NodeDescriptor(0, "127.0.0.1", 9010));

        final WeightedGraph t = new WeightedGraph(nodeList,4);

        t.print();

        int[] precedingNodes = ShortestPath.ShortestPath(t, 0);
        for(int n=0; n<6; n++)
        {
            ShortestPath.printPath(t, precedingNodes, 0, n);
        }
    }
}
