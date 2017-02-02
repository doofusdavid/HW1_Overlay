package cs455.overlay.wireformats;

import cs455.overlay.util.NotImplementedException;

import java.io.IOException;

/**
 * Actual message being sent
 */
public class Message implements Event
{
    private int messageType;


    public Message()
    {
        this.messageType = MessageType.TASK_MESSAGE;
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
