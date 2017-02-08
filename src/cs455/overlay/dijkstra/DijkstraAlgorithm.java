package cs455.overlay.dijkstra;

import java.util.*;

public class DijkstraAlgorithm
{
    private final List<NodeDescriptor> nodes;
    private final List<Edge> edges;
    private Set<NodeDescriptor> settledNodes;
    private Set<NodeDescriptor> unSettledNodes;
    private Map<NodeDescriptor, NodeDescriptor> predecessors;
    private Map<NodeDescriptor, Integer> distance;
    private Graph graph;

    public DijkstraAlgorithm(Graph graph)
    {
        this.nodes = new ArrayList<NodeDescriptor>(graph.getVertexes());
        this.edges = new ArrayList<Edge>(graph.getEdges());
        this.graph = graph;
    }

    public static void main(String args[])
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

//        ArrayList<Edge> edgeList = new ArrayList<>();
//        Random random = new Random();
//
//        edgeList.add(new Edge(nodeList.get(0), nodeList.get(1), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(0), nodeList.get(2), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(0), nodeList.get(3), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(0), nodeList.get(4), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(1), nodeList.get(2), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(2), nodeList.get(3), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(3), nodeList.get(4), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(4), nodeList.get(5), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(5), nodeList.get(6), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(6), nodeList.get(7), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(7), nodeList.get(8), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(8), nodeList.get(9), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(9), nodeList.get(4), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(4), nodeList.get(2), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(5), nodeList.get(7), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(6), nodeList.get(9), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(7), nodeList.get(2), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(8), nodeList.get(3), random.nextInt(10)+1));
//        edgeList.add(new Edge(nodeList.get(9), nodeList.get(4), random.nextInt(10)+1));

//        System.out.println(edgeList);
        Graph graph = new Graph(nodeList, 4);
        System.out.println(graph.getEdges());

        DijkstraAlgorithm da = new DijkstraAlgorithm(graph);
        System.out.println("Edge connection count\n");
        da.printConnectionCount();
        da.execute(nodeList.get(0));
        System.out.println(da.getPath(nodeList.get(7)));

    }

    public void execute(NodeDescriptor source)
    {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        distance.put(source, 0);
        unSettledNodes.add(source);

        while (unSettledNodes.size() > 0)
        {
            NodeDescriptor node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(NodeDescriptor node)
    {
        List<NodeDescriptor> adjacentNodes = getNeighbors(node);

        for (NodeDescriptor target : adjacentNodes)
        {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target))
            {
                distance.put(target, getShortestDistance(node) + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
    }

    private int getDistance(NodeDescriptor node, NodeDescriptor target)
    {
        for (Edge edge : edges)
        {
            if (edge.getSource().equals(node) && edge.getDestination().equals(target))
            {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("GetDistance error: should not occur");
    }

    private int getShortestDistance(NodeDescriptor destination)
    {
        Integer d = distance.get(destination);
        if (d == null)
        {
            return Integer.MAX_VALUE;
        } else
        {
            return d;
        }
    }

    public LinkedList<NodeDescriptor> getPath(NodeDescriptor target)
    {
        LinkedList<NodeDescriptor> path = new LinkedList<>();
        NodeDescriptor step = target;
        if (predecessors.get(step) == null)
        {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null)
        {
            step = predecessors.get(step);
            path.add(step);
        }
        // put in the right order
        Collections.reverse(path);
        return path;
    }

    private List<NodeDescriptor> getNeighbors(NodeDescriptor node)
    {
        List<NodeDescriptor> neighbors = new ArrayList<>();
        for (Edge edge : edges)
        {
            if (edge.getSource().equals(node) && !isSettled(edge.getDestination()))
            {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private boolean isSettled(NodeDescriptor node)
    {
        return settledNodes.contains(node);
    }

    private void printConnectionCount()
    {
        for (NodeDescriptor node : this.nodes)
        {
            System.out.println(node + "-" + this.graph.getConnectionCount(node));
        }
    }

    private NodeDescriptor getMinimum(Set<NodeDescriptor> nodes)
    {
        NodeDescriptor minimum = null;

        for (NodeDescriptor node : nodes)
        {
            if (minimum == null)
            {
                minimum = node;
            } else
            {
                if (getShortestDistance(node) < getShortestDistance(minimum))
                {
                    minimum = node;
                }
            }
        }
        return minimum;
    }
}
