package cs455.overlay.wireformats;

import cs455.overlay.node.Node;

import java.io.IOException;
import java.nio.ByteBuffer;


public class EventFactory
{
    private static final EventFactory instance = new EventFactory();

    private EventFactory()
    {
    }

    public static EventFactory getInstance()
    {
        return instance;
    }

    public void FireEvent(byte[] data, Node node)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        // The length has been trimmed off, so we're just going to start with messageType
        int messageType = byteBuffer.getInt();
        //System.out.println("messageType:" + messageType);
        try
        {
            Event message;
            switch (messageType)
            {
                case MessageType.REGISTER_REQUEST:
                {
                    message = new RegisterRequest(data);
                    break;
                }
                case MessageType.REGISTER_RESPONSE:
                {
                    message = new RegisterResponse(data);
                    break;
                }
                case MessageType.DEREGISTER_REQUEST:
                {
                    message = new DeregisterRequest(data);
                    break;
                }
                case MessageType.LINK_WEIGHTS:
                {
                    message = new LinkWeights(data);
                    break;
                }
                case MessageType.MESSAGING_NODES_LIST:
                {
                    message = new MessagingNodesList(data);
                    break;
                }
                case MessageType.PULL_TRAFFIC_SUMMARY:
                {
                    message = new PullTrafficSummary(data);
                    break;
                }
                case MessageType.TASK_MESSAGE:
                {
                    message = new Message(data);
                    break;
                }
                case MessageType.TASK_INITIATE:
                {
                    message = new TaskInitiate(data);
                    break;
                }
                case MessageType.TASK_COMPLETE:
                {
                    message = new TaskComplete(data);
                    break;
                }
                case MessageType.TRAFFIC_SUMMARY:
                {
                    message = new TrafficSummary(data);
                    break;
                }
                case MessageType.CONNECTION_REQUEST:
                {
                    message = new ConnectionRequest(data);
                    break;
                }
                case MessageType.CONNECTION_RESPONSE:
                {
                    message = new ConnectionResponse(data);
                    break;
                }
                default:
                    System.out.println("Event Factory: Unknown messageType.  Exiting.");
                    return;
            }
            node.onEvent(message);

        } catch (IOException ioe)
        {
            System.out.println("EventFactory.FireEvent " + ioe.getMessage());
        }
    }
}
