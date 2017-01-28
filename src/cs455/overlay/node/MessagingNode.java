package cs455.overlay.node;

import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by david on 1/21/17.
 */
public class MessagingNode implements Node
{
    private TCPReceiverThread receiver;
    private TCPSender sender;
    private int receivedMessageCount;
    private int sentMessageCount;

    @Override
    public void onEvent()
    {
    }

    public MessagingNode()
    {

        try (Socket socket = new Socket(InetAddress.getLocalHost(), 1000))
        {
            receiver = new TCPReceiverThread(socket);
            receiver.run();
        }
        catch (UnknownHostException ue)
        {
            System.out.println(ue.getMessage());
        }
        catch (IOException ie)
        {
            System.out.println(ie.getMessage());
        }

        // Connect to Registry
        // Register self


    }

    /**
     * Create this as synchronized so that two threads can't update the counter simultaneously.
     */
    private synchronized void incrementSentCounter()
    {
        this.sentMessageCount++;
    }
    private synchronized void incrementReceivedCounter()
    {
        this.receivedMessageCount++;
    }
}
