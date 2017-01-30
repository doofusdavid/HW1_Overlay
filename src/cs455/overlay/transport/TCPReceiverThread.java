package cs455.overlay.transport;

import cs455.overlay.node.Node;
import cs455.overlay.wireformats.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * Created by david on 1/21/17.
 */
public class TCPReceiverThread implements Runnable
{
    private ServerSocket serverSocket;
    private Socket socket;
    private Node node;
    private int port;
    private DataInputStream din;

    /**
     *
     * @param      port     the port number, or {@code 0} to use a port
     *                      number that is automatically allocated.
     * @throws IOException
     */
    public TCPReceiverThread(int port, Node node) throws IOException
    {

        try
        {
            // Passing a 0 auto-allocates a port
            this.serverSocket = new ServerSocket(port);
            this.port = this.serverSocket.getLocalPort();
            this.node = node;
            System.out.println(String.format("TCPReceiverThread running on %s", this.serverSocket.getInetAddress().toString()));
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }

    public int getPort()
    {
        return this.port;
    }
    @Override
    public void run()
    {
        int dataLength;
        int messageType;

        while(true)
        {
            try
            {
                Socket socket = serverSocket.accept();
                while(socket!=null)
                {
                    try
                    {
                        din = new DataInputStream(socket.getInputStream());
                        dataLength = din.readInt();
                        //messageType = din.readInt();

                        byte[] data = new byte[dataLength];
                        din.readFully(data, 0, dataLength);
                        EventFactory.FireEvent(data, this.node);

                    } catch (SocketException se)
                    {
                        System.out.println(se.getMessage());
                        break;
                    }
                }
            } catch (IOException ie)
            {
                System.out.println(ie.getMessage());
                break;
            }
        }
    }

}
