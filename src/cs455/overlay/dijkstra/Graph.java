package cs455.overlay.dijkstra;


import java.util.List;

public class Graph
{
    private final List<NodeDescriptor> vertexes;
    private final List<Edge> edges;

    public Graph(List<NodeDescriptor> vertexes, List<Edge> edges)
    {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<NodeDescriptor> getVertexes()
    {
        return vertexes;
    }

    public List<Edge> getEdges()
    {
        return edges;
    }
}
