package cs455.overlay.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/* zxcv asdf *
 * Created by david on 1/30/17.
 */
public class TCPReceiver implements Runnable
{
    private Socket socket;
    private DataInputStream din;

    public TCPReceiver(Socket socket) throws IOException
    {
        this.socket = socket;
        din = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run()
    {
        int dataLength;

        while(socket!=null)
        {
            try
            {
                din = new DataInputStream(socket.getInputStream());
                dataLength = din.readInt();

                byte[] data = new byte[dataLength];
                din.readFully(data, 0, dataLength);
                //ProcessInput(data);

            } catch (SocketException se)
            {
                System.out.println(se.getMessage());
                break;
            } catch (IOException ie)
            {
                System.out.println(ie.getMessage());
                break;
            }
        }
    }
}
