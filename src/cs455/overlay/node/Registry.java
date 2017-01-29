package cs455.overlay.node;

import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.util.IPChecker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by david on 1/21/17.
 */
public class Registry implements Node
{
    private TCPReceiverThread receiver;
    private TCPSender sender;
    private int registryPort;
    private String registryIPAddress;

    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.out.println("Must have one argument, registry Port number.");
            return;
        }

        int argPort;
        // Check for valid registry port
        try
        {
            argPort = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("Invalid Port Number for registry");
            return;
        }

        // Instantiate our registry.
        Registry registry = new Registry(argPort);

        String registryIP="";
        try{
            registryIP = InetAddress.getLocalHost().getHostAddress().toString();
        }
        catch (UnknownHostException e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println(String.format("Registry Started and awaiting orders on port %d and IP address %s.", registry.registryPort, registryIP));

        ProcessInput();

        return;
    }

    private static void ProcessInput()
    {
        Scanner in = new Scanner(System.in);
        while(true)
        {
            String input = in.nextLine();
            if(input.startsWith("setup-overlay"))
            {
                System.out.println("Received Setup Overlay command");

                // Setup overlay

                break;
            }
            if(input.startsWith("start"))
            {
                // split the string by any whitespace to get the number of rounds to perform.
                String[] startCommand = input.split("\\s+");
                if(startCommand.length != 2)
                {
                    System.out.println("Received Start command with invalid parameters.  Please try 'start <number of rounds>");
                    break;
                }
                else
                {
                    try
                    {
                        int numRounds = Integer.parseInt(startCommand[1]);
                        System.out.println(String.format("Received Start command.  Starting %d rounds.", numRounds));

                        // start the process

                        continue;
                    }
                    catch (NumberFormatException nfe)
                    {
                        System.out.println("Received Start command with invalid parameters.  Please try 'start <number of rounds>");
                        continue;
                    }
                }
            }

            switch (input)
            {
                case "list-messaging-nodes":
                    System.out.println("Received List Messaging Nodes command");

                    // List Messaging nodes

                    break;
                case "list-weights":
                    System.out.println("Received List Weights command");

                    // List Weights
                    break;
                case "send-overlay-link-weights":
                    System.out.println("Received Send Overlay Link Weights command");

                    // Send Overlay Link Weights
                    break;
                default:
                    System.out.println("Unknown command.\nKnown commands are print-shortest-path and exit-overlay");
                    break;
            }
        }
    }

    public Registry(int port)
    {
        this.registryPort = port;

        try
        {
            TCPReceiverThread receiver = new TCPReceiverThread(this.registryPort);
            this.registryIPAddress = InetAddress.getLocalHost().getHostAddress().toString();
            receiver.run();
        }
        catch (UnknownHostException ue)
        {
            System.out.println(ue.getMessage());
        }
        catch (IOException ie)
        {
            System.out.println(ie.getMessage());
        }

    }


    @Override
    public void onEvent()
    {

    }
}
