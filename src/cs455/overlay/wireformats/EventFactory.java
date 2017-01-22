package cs455.overlay.wireformats;

/**
 * Created by david on 1/21/17.
 */
public class EventFactory
{
    private static EventFactory ourInstance = new EventFactory();

    public static EventFactory getInstance()
    {
        return ourInstance;
    }

    private EventFactory()
    {
    }
}
