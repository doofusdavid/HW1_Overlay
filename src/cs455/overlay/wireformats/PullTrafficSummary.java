package cs455.overlay.wireformats;

import cs455.overlay.util.NotImplementedException;

import java.io.IOException;


public class PullTrafficSummary implements Event
{
    private int messageType;

    public PullTrafficSummary()
    {
        this.messageType = MessageType.PULL_TRAFFIC_SUMMARY;
    }

    public PullTrafficSummary(byte[] data)
    {
        this();
        throw new NotImplementedException();
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        throw new NotImplementedException();
    }

    @Override
    public int getType()
    {
        return this.messageType;
    }
}
