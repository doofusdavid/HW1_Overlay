package cs455.overlay.util;

import java.util.regex.Pattern;

/**
 * Quick class to determine if a valid IP is being passed in
 */
public class IPChecker
{
    private static final Pattern IPPATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate (final String ipAddress)
    {
        return IPPATTERN.matcher(ipAddress).matches();
    }
}
