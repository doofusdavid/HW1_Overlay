package cs455.overlay.wireformats;

/**
 * Created by david on 1/28/17.
 */
public class MessageType
{
    public static final int REGISTER_REQUEST = 0;
    public static final int REGISTER_RESPONSE = 1;
    public static final int DEREGISTER_REQUEST = 2;
    public static final int MESSAGING_NODES_LIST = 3;
    public static final int LINK_WEIGHTS = 4;
    public static final int TASK_INITIATE = 5;
    public static final int TASK_COMPLETE = 6;
    public static final int PULL_TRAFFIC_SUMMARY = 7;
    public static final int TRAFFIC_SUMMARY = 8;
    public static final int TASK_MESSAGE = 9;
    public static final int DEREGISTER_RESPONSE =  10;
    public static final int NODE_DESCRIPTOR = 11;
}
