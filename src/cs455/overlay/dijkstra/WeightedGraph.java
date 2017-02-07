package cs455.overlay.dijkstra;

import java.util.ArrayList;
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

    public int indexOfNode(NodeDescriptor node)
    {
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i] == node)
            {
                return i;
            }
        }
        return -1;
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

        // TODO: Ensure there are no partitions, by connecting 1 to 2, 2 to 3, etc

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
                if(cCount[node.Index] >= connectionCount)
                {
                    break;
                }
                else
                {
                    int nodeToLink = deck.get(i);

                    // skip if it would link to itself, or the node we're
                    // linking to already has enough connections
                    if ((nodeToLink == node.Index) ||
                            (cCount[nodeToLink] >= connectionCount))
                    {
                        i++;
                        continue;
                    } else
                    {
                        // add an edge and increment the count of connections
                        this.addEdge(node.Index, nodeToLink, random.nextInt(10) + 1);
                        cCount[node.Index]++;
                        cCount[nodeToLink]++;
                        i++;
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
        edges[target][source] = weight;
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

    public int getWeight(NodeDescriptor source, NodeDescriptor target)
    {
        return edges[indexOfNode(source)][indexOfNode(target)];
    }

    /**
     * Gets the neighbor nodes of the one sent in
     *
     * @param vertex
     * @return int array of the neighbor nodes
     */
    public int[] neighbors (int vertex)
    {
        int count = 0;
        for (int i = 0; i<edges[vertex].length; i++)
        {
            if(edges[vertex][i]>0)
                count++;
        }
        final int[]answer = new int[count];
        count = 0;
        for(int i = 0; i<edges[vertex].length; i++)
        {
            if(edges[vertex][i]>0)
                answer[count++] = i;
        }
        return answer;
    }

    public void print()
    {
        for(int i = 0; i<edges.length; i++)
        {
            System.out.print(nodes[i].toString() + ":");
            for(int j = 0; j<edges[i].length; j++)
            {
                if(edges[i][j]>0)
                {
                    System.out.print(nodes[j].Index + " - " + edges[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Used by Messaging Nodes to rehydrate a WeightedGraph frmo a LinkWeights
     * message
     *
     * @param nodeWeights the list of NodeWeights sent by the registry
     */
    public WeightedGraph(ArrayList<NodeWeight> nodeWeights)
    {
        ArrayList<NodeDescriptor> nodes = new ArrayList<>();

        // first, get all the distinct nodes
        for (NodeWeight nw : nodeWeights)
        {
            if (!nodes.contains(nw.source))
            {
                nodes.add(nw.source);
            }
        }
        this.nodes = nodes.toArray(new NodeDescriptor[nodes.size()]);
        this.edges = new int[nodes.size()][nodes.size()];

        for (NodeWeight nw : nodeWeights)
        {
            int i = nodes.indexOf(nw.source);
            int j = nodes.indexOf(nw.destination);
            this.edges[i][j] = nw.weight;
            this.edges[j][i] = nw.weight;
        }
    }

    public ArrayList<NodeWeight> toNodeWeights()
    {
        ArrayList<NodeWeight> weights = new ArrayList<>(edges.length);
        for (int i = 0; i < edges.length; i++)
        {
            NodeDescriptor source = nodes[i];
            for (int j = 0; j < edges[i].length; j++)
            {
                if (edges[i][j] > 0)
                {
                    NodeDescriptor destination = nodes[j];
                    weights.add(new NodeWeight(source, destination, edges[i][j]));
                }
            }
        }
        return weights;
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

        final WeightedGraph graph = new WeightedGraph(nodeList,4);

        graph.print();

        int[] precedingNodes = ShortestPath.ShortestPath(graph, 0);
        ArrayList<NodeDescriptor> precedingNodeList = ShortestPath.ShortestPath(graph, nodeList.get(0));

        for (NodeDescriptor n : graph.nodes)
        {
            ShortestPath.printPath(graph, precedingNodeList, nodeList.get(0), n);
        }
        for(int n = 0; n<graph.size(); n++)
        {
            ShortestPath.printPath(graph, precedingNodes, 0, n);
        }
    }

    public NodeDescriptor[] neighbors(NodeDescriptor next)
    {
        int count = 0;
        int nodeIndex = indexOfNode(next);
        for (int i = 0; i < edges[nodeIndex].length; i++)
        {
            if (edges[nodeIndex][i] > 0)
                count++;
        }
        final NodeDescriptor[] answer = new NodeDescriptor[count];
        count = 0;
        for (int i = 0; i < edges[nodeIndex].length; i++)
        {
            if (edges[nodeIndex][i] > 0)
                answer[count++] = nodes[i];
        }
        return answer;
    }
}
