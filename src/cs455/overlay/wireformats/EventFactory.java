package cs455.overlay.wireformats;

import cs455.overlay.node.Node;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by david on 1/21/17.
 */
public class EventFactory
{
    private static EventFactory ourInstance = new EventFactory();

    public static EventFactory getInstance()
    {
        return ourInstance;
    }

    public static void FireEvent(byte[] data, Node node)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        // The length has been trimmed off, so we're just going to start with messageType
        int messageType = byteBuffer.getInt();

        try
        {

            switch (messageType)
            {
                case MessageType.REGISTER_REQUEST:
                {
                    RegisterRequest message = new RegisterRequest(data);
                    node.onEvent(message);
                    break;
                }
                case MessageType.DEREGISTER_REQUEST:
                {
                    Deregister message = new Deregister(data);
                    break;
                }
            }
        } catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }
}
