package cs455.overlay.wireformats;

import java.io.IOException;

/**
 * Interface containing the methods used by Events
 */
public interface Event
{

    int getType();
    byte[] getBytes() throws IOException;

}
