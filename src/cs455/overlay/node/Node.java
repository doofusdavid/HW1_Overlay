package cs455.overlay.node;

import cs455.overlay.wireformats.Event;

/**
 * Created by david on 1/21/17.
 */
public interface Node
{

    void onEvent(Event event);
}
