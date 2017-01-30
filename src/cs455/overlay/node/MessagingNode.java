package cs455.overlay.node;

import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.util.IPChecker;
import cs455.overlay.wireformats.RegisterRequest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by david on 1/21/17.
 */
public class MessagingNode implements Node
{
    private TCPReceiverThread receiver;
    private TCPSender sender;
    private String hostIPAddress;
    private int hostPort;
    private String registryIPAddress;
    private int registryPort;

    // keep track of number of messages sent/receive by this node
    private int receiveTracker;
    private int sendTracker;

    // keep track of the sum of the random numbers sent
    private long sendSummation;
    private long receiveSummation;


    @Override
    public void onEvent()
    {
    }

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Must have two arguments, registry host IP and registry Port number.");
            return;
        }

        // Check for valid registry IP address
        String registryIPAddress = args[0];
        if(!(IPChecker.validate(registryIPAddress)))
        {
            System.out.println("Invalid IP Address for registry");
            return;
        }

        // Check for valid registry port
        int registryPort;
        try
        {
            registryPort = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("Invalid Port Number for registry");
            return;
        }

        MessagingNode node = new MessagingNode(registryIPAddress, registryPort);

        System.out.println("Node Started and awaiting orders.");

        ProcessInput();
        return;
    }

    private static void ProcessInput()
    {
        Scanner in = new Scanner(System.in);
        while(true)
        {
            String input = in.nextLine();
            switch (input)
            {
                case "print-shortest-path":
                    System.out.println("Received Print Shortest Path command");
                    break;
                case "exit-overlay":
                    System.out.println("Received Exit Overlay command");

                    // Send deregistration message to registry
                    // await response
                    // exit and terminate process
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command.\nKnown commands are print-shortest-path and exit-overlay");
                    break;
            }
        }
    }


    public MessagingNode(String registryIPAddress, int registryPort)
    {
        this.registryIPAddress = registryIPAddress;
        // Debugging

        this.registryPort = registryPort;

        try
        {
            TCPReceiverThread receiver = new TCPReceiverThread(0);
            this.hostPort = receiver.getPort();
            this.hostIPAddress = InetAddress.getLocalHost().toString();
            Thread t = new Thread(receiver);
            t.start();
            System.out.println(String.format("Messaging Node: %d started TCPReceiverThread",this.hostPort));
            //receiver.run();
        }
        catch (UnknownHostException ue)
        {
            System.out.println(ue.getMessage());
        }
        catch (IOException ie)
        {
            System.out.println(ie.getMessage());
        }

        try
        {
            System.out.println(String.format("Attempting to connect to registry at: %s:%d", this.registryIPAddress, this.registryPort));
            Socket registrySocket = new Socket(this.registryIPAddress, this.registryPort);
            TCPSender sender = new TCPSender(registrySocket);
            RegisterRequest registerRequestMessage = new RegisterRequest();
            registerRequestMessage.IPAddress = this.hostIPAddress;
            registerRequestMessage.Port = this.hostPort;

            sender.sendData(registerRequestMessage.getBytes());

        } catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
        // Connect to Registry
        // RegisterRequest self


    }

    /**
     * Create this as synchronized so that two threads can't update the counter simultaneously.
     */
    private synchronized void incrementSentCounter()
    {
        this.sendTracker++;
    }
    private synchronized void incrementReceivedCounter()
    {
        this.receiveTracker++;
    }
    private synchronized void addReceiveSummation(int value)
    {
        this.receiveSummation += value;
    }
    private synchronized void addSentSummation(int value)
    {
        this.sendSummation += value;
    }
}
