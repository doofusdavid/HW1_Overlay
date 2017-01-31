package cs455.overlay.wireformats;

import cs455.overlay.util.NotImplementedException;

import java.io.IOException;


public class MessagingNodesList implements Event
{
    private int messageType;

    public MessagingNodesList(byte[] data)
    {
        this();
        throw new NotImplementedException();
    }

    public MessagingNodesList()
    {
        this.messageType = MessageType.LINK_WEIGHTS;
    }

    @Override
    public int getType()
    {
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        return new byte[0];
    }
}
