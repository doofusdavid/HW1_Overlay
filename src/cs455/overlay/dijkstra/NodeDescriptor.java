package cs455.overlay.dijkstra;

import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.MessageType;

import java.io.IOException;

/**
 * Node Descriptor is a lightweight class representing a single node, used in computing
 * paths.
 */
public class NodeDescriptor implements Event
{
    public final int type = MessageType.NODE_DESCRIPTOR;
    public int Index;
    public String IPAddress;
    public int Port;

    public NodeDescriptor(String element)
    {
        this(element.getBytes());
    }

    @Override
    public String toString()
    {
        return String.format("%s:%d", IPAddress, Port);
    }

    public String toExtendedString()
    {
        return String.format("%d:%s:%d:", Index, IPAddress, Port);
    }

    public NodeDescriptor(int Index, String IPAddress, int Port)
    {
        this.Index = Index;
        this.IPAddress = IPAddress;
        this.Port = Port;
    }

    public NodeDescriptor(byte[] marshalledBytes)
    {
        String descriptor = new String(marshalledBytes);
        String[] nodeParts = descriptor.split(":");
        if (nodeParts.length == 2)
        {
            this.Index = 0;  // We aren't sending index over the wire
            this.IPAddress = nodeParts[0];
            this.Port = Integer.parseInt(nodeParts[1]);
        } else
        {
            throw new IllegalArgumentException("Bytes did not construct a valid node.");
        }
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

    @Override
    public int getType()
    {
        return this.type;
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        return this.toString().getBytes();
    }
}
