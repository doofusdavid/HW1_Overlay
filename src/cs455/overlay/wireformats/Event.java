package cs455.overlay.wireformats;

import java.io.IOException;

/**
 * Created by david on 1/21/17.
 */
public interface Event
{
    public static enum MessageType
    {
        REGISTER_REQUEST,
        REGISTER_RESPONSE,
        DEREGISTER_REQUEST,
        MESSAGING_NODES_LIST,
        LINK_WEIGHTS,
        TASK_INITIATE,
        TASK_COMPLETE,
        PULL_TRAFFIC_SUMMARY,
        TRAFFIC_SUMMARY

    }
    int getType();
    byte[] getBytes() throws IOException;

}
