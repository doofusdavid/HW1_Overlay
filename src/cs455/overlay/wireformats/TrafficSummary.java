package cs455.overlay.wireformats;

import java.io.*;

/**
 * Created by david on 1/21/17.
 */
public class TrafficSummary implements Event
{
    private final int type = MessageType.TRAFFIC_SUMMARY;
    private String IPAddress;
    private int Port;
    private int MessageSentCount;
    private long MessageSentSummary;
    private int MessageReceivedCount;
    private long MessageReceivedSummary;
    private int MessageRelayedCount;

    public TrafficSummary(String IPAddress, int port, int messageSentCount, long messageSentSummary, int messageReceivedCount, long messageReceivedSummary, int messageRelayedCount)
    {
        this.IPAddress = IPAddress;
        Port = port;
        MessageSentCount = messageSentCount;
        MessageSentSummary = messageSentSummary;
        MessageReceivedCount = messageReceivedCount;
        MessageReceivedSummary = messageReceivedSummary;
        MessageRelayedCount = messageRelayedCount;
    }

    public TrafficSummary(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.TRAFFIC_SUMMARY)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a TrafficSummary.");
        }

        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);
        this.IPAddress = new String(ipBytes);

        this.Port = din.readInt();

        this.MessageSentCount = din.readInt();
        this.MessageSentSummary = din.readLong();
        this.MessageReceivedCount = din.readInt();
        this.MessageReceivedSummary = din.readLong();
        this.MessageRelayedCount = din.readInt();

        baInputStream.close();
        din.close();

    }

    @Override
    public int getType()
    {
        return this.type;
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.type);

        byte[] ipBytes = this.IPAddress.getBytes();
        int ipLength = ipBytes.length;
        dout.writeInt(ipLength);
        dout.write(ipBytes);

        dout.writeInt(this.Port);

        dout.writeInt(this.MessageSentCount);
        dout.writeLong(this.MessageSentSummary);
        dout.writeInt(this.MessageReceivedCount);
        dout.writeLong(this.MessageReceivedSummary);
        dout.writeInt(this.MessageRelayedCount);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();

        return marshalledBytes;
    }
}
