package cs455.overlay.dijkstra;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Weighted graph consisting of nodes and edges.  Used as an input to Dijkstra's
 * algorithm, and can be rehydrated by MessagingNodes.
 */
public class Graph
{
    private final List<NodeDescriptor> vertexes;
    private final List<Edge> edges;

    public Graph(List<NodeDescriptor> vertexes, List<Edge> edges)
    {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public Graph(List<NodeDescriptor> nodes, int connections)
    {
        this.vertexes = nodes;
        Random random = new Random();
        this.edges = new ArrayList<>();
        int[] cCount = new int[nodes.size()];

        // first ensure no partitions.  Every node connected in a circle
        for (int i = 0; i < this.vertexes.size() - 1; i++)
        {
            edges.add(new Edge(this.vertexes.get(i), this.vertexes.get(i + 1), random.nextInt(10) + 1));
            cCount[i]++;
            cCount[i + 1]++;
        }
        edges.add(new Edge(this.vertexes.get(nodes.size() - 1), this.vertexes.get(0), random.nextInt(10) + 1));
        cCount[nodes.size() - 1]++;
        cCount[0]++;


        ArrayList<Integer> deck = new ArrayList<Integer>();
        for (int i = 0; i < this.vertexes.size(); i++)
        {
            deck.add(i);
        }

        for (NodeDescriptor node : this.vertexes)
        {
            Collections.shuffle(deck);

            for (int i = 0; i < this.vertexes.size(); i++)
            {
                if (cCount[this.vertexes.indexOf(node)] >= connections)
                {
                    break;
                } else
                {
                    int nodeToLink = deck.get(i);

                    // skip if it would link to itself, or the node we're
                    // linking to already has enough connections
                    if (this.vertexes.get(nodeToLink).equals(node) ||
                            (cCount[nodeToLink] >= connections))
                    {
                        i++;
                        continue;
                    } else
                    {
                        // add an edge and increment the count of connections
                        this.edges.add(new Edge(node, this.vertexes.get(nodeToLink), random.nextInt(10) + 1));
                        cCount[this.vertexes.indexOf(node)]++;
                        cCount[nodeToLink]++;
                        i++;
                    }
                }
            }
        }

        // See if there are any missing connections we can make up
        for (int i = 0; i < this.vertexes.size(); i++)
        {
            if (cCount[i] < connections)
            {
                for (int j = i + 1; j < this.vertexes.size(); j++)
                {
                    if (cCount[j] < connections)
                    {
                        Edge newEdge = new Edge(this.vertexes.get(i), this.vertexes.get(j), random.nextInt(10) + 1);
                        for (Edge edge : this.edges)
                        {
                            if (edge.equals(newEdge))
                                continue;
                            else
                            {
                                this.edges.add(newEdge);
                                cCount[i]++;
                                cCount[j]++;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public Graph(ArrayList<Edge> edges)
    {
        ArrayList<NodeDescriptor> nodes = new ArrayList<>();

        // first, get all the distinct nodes
        for (Edge edge : edges)
        {
            if (!nodes.contains(edge.getSource()))
            {
                nodes.add(edge.getSource());
            }
        }
        this.vertexes = new ArrayList<>(nodes);
        this.edges = new ArrayList<>();

        for (Edge edge : edges)
        {
            this.edges.add(new Edge(edge.getSource(), edge.getDestination(), edge.getWeight()));
        }

    }

    public List<NodeDescriptor> getVertexes()
    {
        return vertexes;
    }

    public int getOrder()
    {
        return this.vertexes.size();
    }

    public int getSize()
    {
        return this.edges.size();
    }

    /**
     * Return the number of connections to/from the given node
     *
     * @param node
     * @return
     */
    public int getConnectionCount(NodeDescriptor node)
    {
        int count = 0;
        for (Edge edge : edges)
        {
            if (edge.contains(node))
                count++;
        }
        return count;
    }

    /**
     * Returns list of nodes that a node should connect to.  Not duplicated, so this will contain
     * between connectioncount and 1 nodes.
     *
     * @param node
     * @return list of nodes that a node should connect to.
     */
    public List<NodeDescriptor> getNeighbors(NodeDescriptor node)
    {
        ArrayList<NodeDescriptor> neighbors = new ArrayList<>();

        for (Edge edge : edges)
        {
            if (edge.getSource().equals(node))
            {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    public List<Edge> getEdges()
    {
        return edges;
    }

    public void printEdges()
    {
        for (Edge edge : edges)
        {
            System.out.println(edge);
        }
    }
}
