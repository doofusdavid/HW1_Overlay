package cs455.overlay.dijkstra;

/**
 * Node Descriptor is a lightweight class representing a single node, used in computing
 * paths.
 */
public class NodeDescriptor
{
    public int Index;
    public String IPAddress;
    public int Port;

    @Override
    public String toString()
    {
        return String.format("%d:%s:%d: ", Index, IPAddress, Port);
    }
}
