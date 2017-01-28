package cs455.overlay.node;

import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;

/**
 * Created by david on 1/21/17.
 */
public class MessagingNode implements Node
{
    private TCPReceiverThread receiver;
    private TCPSender sender;


    @Override
    public void onEvent()
    {
    }

    public MessagingNode()
    {
        // Connect to Registry
        // Register self


    }
}
