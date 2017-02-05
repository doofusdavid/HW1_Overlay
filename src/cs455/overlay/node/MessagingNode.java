package cs455.overlay.node;

import cs455.overlay.dijkstra.NodeWeight;
import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.util.IPChecker;
import cs455.overlay.util.NotImplementedException;
import cs455.overlay.wireformats.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MessagingNode implements Node
{
    private TCPReceiverThread receiver;
    private TCPSender sender;
    private String hostIPAddress;
    private int hostPort;
    private String registryIPAddress;
    private int registryPort;
    private boolean connectedToRegistry;
    private ArrayList<NodeWeight> weights;

    // keep track of number of messages sent/receive by this node
    private int receiveTracker;
    private int sendTracker;

    // keep track of the sum of the random numbers sent
    private long sendSummation;
    private long receiveSummation;

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

        ProcessInput(node);
    }

    private static void ProcessInput(MessagingNode node)
    {
        Scanner in = new Scanner(System.in);
        while(true)
        {
            String input = in.nextLine();
            switch (input)
            {
                case "print-shortest-path":
                    System.out.println("Received Print Shortest Path command");
                    throw new NotImplementedException();
                    //break;
                case "exit-overlay":
                    System.out.println("Received Exit Overlay command");

                    node.SendExitOverlay();
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


    private MessagingNode(String registryIPAddress, int registryPort)
    {
        this.registryIPAddress = registryIPAddress;
        this.registryPort = registryPort;
        this.connectedToRegistry = false;

        try
        {
            TCPReceiverThread receiver = new TCPReceiverThread(0, this);
            this.hostPort = receiver.getPort();
            this.hostIPAddress = InetAddress.getLocalHost().getHostAddress();
            Thread t = new Thread(receiver);
            t.start();
            System.out.println(String.format("Messaging Node: %d started TCPReceiverThread",this.hostPort));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        // Connect to Registry
        this.SendRegistrationRequest();

    }

    private void SendRegistrationRequest()
    {
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
    }

    private void SendExitOverlay()
    {
        try
        {
            System.out.println(String.format("Attempting to send exit registry at: %s:%d", this.registryIPAddress, this.registryPort));
            Socket registrySocket = new Socket(this.registryIPAddress, this.registryPort);
            TCPSender sender = new TCPSender(registrySocket);
            DeregisterRequest deregisterRequestMessage = new DeregisterRequest();
            deregisterRequestMessage.Port = this.hostPort;
            deregisterRequestMessage.IPAddress = this.hostIPAddress;

            sender.sendData(deregisterRequestMessage.getBytes());

        } catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }

    private void ReceiveRegistrationResponse(RegisterResponse response)
    {
        if(response.statusCode== StatusCode.FAILURE)
        {
            System.out.println("Registration Request Failed.  Exiting.");
            System.out.println(String.format("Message: %s", response.additionalInfo));
            System.exit(0);
        }
        else
        {
            System.out.println("Registration Request Succeeded.");
            System.out.println(String.format("Message: %s", response.additionalInfo));
            this.connectedToRegistry = true;
        }
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

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof RegisterResponse)
        {
            this.ReceiveRegistrationResponse((RegisterResponse) event);
        }
        if (event instanceof LinkWeights)
        {
            this.ReceiveLinkWeights((LinkWeights) event);
        }
        if (event instanceof Message)
        {
            this.ReceiveMessage((Message) event);
        }
    }

    private void ReceiveMessage(Message event)
    {
        throw new NotImplementedException();
    }

    private void ReceiveLinkWeights(LinkWeights event)
    {
        System.out.println("Link Weights Received");
        this.weights = event.nodeWeights;
    }
}
