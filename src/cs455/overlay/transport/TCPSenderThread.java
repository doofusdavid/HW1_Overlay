package cs455.overlay.transport;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by david on 2/10/2017.
 */
public class TCPSenderThread implements Runnable
{
    private String IPAddress;
    private int Port;
    private byte[] dataToSend;

    public TCPSenderThread(String IPAddress, int Port, byte[] dataToSend)
    {
        this.IPAddress = IPAddress;
        this.Port = Port;
        this.dataToSend = dataToSend;
    }

    @Override
    public void run()
    {
        try
        {
            Socket socket = new Socket(IPAddress, Port);
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            int dataLength = dataToSend.length;
            dout.writeInt(dataLength);

            dout.write(dataToSend, 0, dataLength);
            dout.flush();
            dout.close();
            socket.close();
        } catch (Exception e)
        {
            System.out.println("TCPSenderThread: " + e.getMessage());
        }

    }
}
