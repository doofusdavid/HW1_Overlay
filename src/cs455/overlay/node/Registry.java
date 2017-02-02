package cs455.overlay.node;

import cs455.overlay.dijkstra.NodeDescriptor;
import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.util.NotImplementedException;
import cs455.overlay.wireformats.*;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.RegisterRequest;
import cs455.overlay.wireformats.RegisterResponse;
import cs455.overlay.wireformats.StatusCode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
    private ArrayList<NodeDescriptor> nodeList;

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
            registryIP = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println(String.format("Registry Started and awaiting orders on port %d and IP address %s.", registry.registryPort, registryIP));

        ProcessInput(registry);

    }

    private static void ProcessInput(Registry registry)
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
                    registry.ListMessagingNodes();

                    break;
                case "list-weights":
                    System.out.println("Received List Weights command");
                    throw new NotImplementedException();
                    // List Weights
                    //break;
                case "send-overlay-link-weights":
                    System.out.println("Received Send Overlay Link Weights command");
                    throw new NotImplementedException();

                    // Send Overlay Link Weights
                    //break;
                default:
                    System.out.println("Unknown command.\nKnown commands are print-shortest-path and exit-overlay");
                    break;
            }
        }
    }

    private void ListMessagingNodes()
    {
        if(this.nodeList.size() == 0)
        {
            System.out.println("list-messaging-nodes: There are no registered Messaging Nodes");
        }
        else
        {
            System.out.println(String.format("list-messaging-nodes: There are %d total registered Messaging Nodes", this.nodeList.size()));
            for(NodeDescriptor item : this.nodeList)
            {
                System.out.println("list-messaging-nodes: Node: "+ item.toString());
            }
        }
    }

    private Registry(int port)
    {
        this.registryPort = port;
        this.nodeList = new ArrayList<>();

        try
        {
            TCPReceiverThread receiver = new TCPReceiverThread(this.registryPort, this);
            this.registryIPAddress = InetAddress.getLocalHost().getHostAddress();

            Thread t = new Thread(receiver);
            t.start();
            System.out.println("Registry TCPReceiverThread running.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void RegisterNode(RegisterRequest request)
    {
        System.out.println("Register Request Fired");
        if(this.nodeList.contains(request.Port))
        {
            // Node already registered, send a Failure response
            this.SendRegistrationResponse(request, StatusCode.FAILURE, "Node already registered.  No action taken");
        }
        else
        {
            // Add the port and send an affirmative response.
            this.nodeList.add(new NodeDescriptor(0, request.IPAddress, request.Port));
            this.SendRegistrationResponse(request, StatusCode.SUCCESS, "Node Registered");
        }
    }

    private void SendRegistrationResponse(RegisterRequest request, byte status, String message)
    {
        try
        {
            System.out.println(String.format("Sending Registration Response to %s:%d", request.IPAddress, request.Port));
            Socket socket = new Socket(request.IPAddress, request.Port);
            TCPSender sender = new TCPSender(socket);

            RegisterResponse response = new RegisterResponse();
            response.statusCode = status;
            response.additionalInfo = message;

            sender.sendData(response.getBytes());

        } catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }

    @Override
    public void onEvent(Event event)
    {
        if(event instanceof RegisterRequest)
        {
            RegisterNode((RegisterRequest)event);
        }
        else if(event instanceof DeregisterRequest)
        {
            DeRegisterNode((DeregisterRequest) event);
        }
    }

    private void DeRegisterNode(DeregisterRequest event)
    {
        System.out.println("DeRegister Request Fired");
        NodeDescriptor node = new NodeDescriptor(0,event.IPAddress, event.Port);

        if(this.nodeList.contains(node))
        {
            this.nodeList.remove(node);
            this.SendDeRegisterResponse(event, StatusCode.SUCCESS, "Node Deregistered");
        }
        else
        {
            this.SendDeRegisterResponse(event, StatusCode.FAILURE, "That node is not registered.");
        }
    }

    private void SendDeRegisterResponse(DeregisterRequest event, byte status, String message)
    {
        try
        {
            System.out.println(String.format("Sending Deregistration Response to %s:%d", event.IPAddress, event.Port));
            Socket socket = new Socket(event.IPAddress, event.Port);
            TCPSender sender = new TCPSender(socket);

            DeregisterResponse response = new DeregisterResponse();
            response.statusCode = status;
            response.additionalInfo = message;

            sender.sendData(response.getBytes());

        } catch (IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }


}
