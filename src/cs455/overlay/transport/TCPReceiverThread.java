package cs455.overlay.transport;

import com.sun.corba.se.spi.activation.Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by david on 1/21/17.
 */
public class TCPReceiverThread implements Runnable
{
    private ServerSocket serverSocket;
    private int port;
    private DataInputStream din;

    /**
     *
     * @param      port     the port number, or {@code 0} to use a port
     *                      number that is automatically allocated.
     * @throws IOException
     */
    public TCPReceiverThread(int port) throws IOException
    {

        try
        {
            // Passing a 0 auto-allocates a port
            this.serverSocket = new ServerSocket(port);
            this.port = this.serverSocket.getLocalPort();
        }
        catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }

    @Override
    public void run()
    {
        int dataLength;

        while(true)
        {
            try
            {
                Socket socket = serverSocket.accept();
                while(socket!=null)
                {
                    try
                    {
                        dataLength = din.readInt();

                        byte[] data = new byte[dataLength];
                        din.readFully(data, 0, dataLength);
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
