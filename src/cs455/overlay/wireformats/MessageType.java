package cs455.overlay.wireformats;

/**
 * Constants containing all MessageTypes for the various Events
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
    public static final int CONNECTION_REQUEST = 12;
    public static final int CONNECTION_RESPONSE = 13;
}
