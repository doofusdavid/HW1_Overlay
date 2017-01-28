package cs455.overlay.node;

import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;

/**
 * Created by david on 1/21/17.
 */
public class Registry implements Node
{
    private TCPReceiverThread receiver;
    private TCPSender sender;

    @Override
    public void onEvent()
    {

    }
}
