package cs455.overlay.wireformats;

import cs455.overlay.util.NotImplementedException;

import java.io.IOException;


public class LinkWeights implements Event
{
    private int messageType;

    public LinkWeights()
    {
        this.messageType = MessageType.LINK_WEIGHTS;
    }

    public LinkWeights(byte[] data)
    {
        this();
        throw new NotImplementedException();
    }

    @Override
    public int getType()
    {
        throw new NotImplementedException();
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        throw new NotImplementedException();
    }
}
