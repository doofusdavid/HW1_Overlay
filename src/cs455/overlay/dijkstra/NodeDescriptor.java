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
        return String.format("%d:%s:%d:", Index, IPAddress, Port);
    }

    public NodeDescriptor(int Index, String IPAddress, int Port)
    {
        this.Index = Index;
        this.IPAddress = IPAddress;
        this.Port = Port;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj instanceof NodeDescriptor)
        {
            if (((NodeDescriptor) obj).IPAddress == this.IPAddress && ((NodeDescriptor) obj).Port == this.Port)
            {
                return true;
            }
        }
        return false;
    }
}
