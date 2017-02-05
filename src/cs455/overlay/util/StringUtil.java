package cs455.overlay.util;


public class StringUtil
{

    public static int getIntFromStringCommand(String command)
    {
        String[] commandArray = command.split("\\s+");
        if(commandArray.length != 2)
            return -1;
        try
        {
            int commandInt = Integer.parseInt(commandArray[1]);
            return commandInt;
        }
        catch (NumberFormatException nfe)
        {
            return -1;
        }
    }
}
