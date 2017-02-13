package cs455.overlay.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * RoutingCache creates a cache of the routing path from one node to another.
 * For larger runs, this creates fewer instances of calling
 * the DijkstraAlgorithm class, speeding up the routing.
 */
public class RoutingCache
{
    private Map<NodeDescriptor, ArrayList<NodeDescriptor>> cache;

    public RoutingCache()
    {
        cache = new HashMap<>();
    }

    public void addPath(NodeDescriptor sink, ArrayList<NodeDescriptor> routingPath)
    {
        if (cache.containsKey(sink))
        {
            return;
        } else
        {
            cache.put(sink, routingPath);
        }
    }

    public boolean routeExists(NodeDescriptor node)
    {
        return cache.containsKey(node);
    }

    public ArrayList<NodeDescriptor> getPath(NodeDescriptor sink)
    {
        if (!cache.containsKey(sink))
            throw new IllegalArgumentException("Values does not exist in the cache");
        return cache.get(sink);
    }
}
