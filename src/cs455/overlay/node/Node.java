package cs455.overlay.node;

import cs455.overlay.wireformats.Event;

/**
 * Interface containing the basics used by Nodes, currently Registry and MessagingNode
 */
public interface Node
{

    void onEvent(Event event);
}
