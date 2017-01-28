package cs455.overlay.wireformats;

import java.io.IOException;

/**
 * Created by david on 1/21/17.
 */
public interface Event
{

    int getType();
    byte[] getBytes() throws IOException;

}
