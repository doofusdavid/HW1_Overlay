package cs455.overlay.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by david on 1/21/17.
 */
public class TCPSender
{
    private Socket socket;
    private DataOutputStream dout;

    public TCPSender(Socket socket) throws IOException
    {
        this.socket = socket;
        this.socket.setTcpNoDelay(true);
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public synchronized void sendData(byte[] dataToSend) throws IOException
    {
        int dataLength = dataToSend.length;
        dout.writeInt(dataLength);

        dout.write(dataToSend,0,dataLength);
        dout.flush();

    }
}
