package cs455.overlay.node;

import cs455.overlay.dijkstra.*;
import cs455.overlay.transport.TCPReceiverThread;
import cs455.overlay.transport.TCPSender;
import cs455.overlay.util.IPChecker;
import cs455.overlay.util.StringUtil;
import cs455.overlay.wireformats.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;

/**
 * One of the two main classes used for HW1.  Communicates with the Registry, and when told
 * MessagingNodes communicate with other MessagingNodes, and returns statistics to the Registry
 */
public class MessagingNode implements Node
{
    private String hostIPAddress;
    private int hostPort;
    private String registryIPAddress;
    private int registryPort;
    private boolean connectedToRegistry;
    private ArrayList<Edge> edges;
    private ArrayList<NodeDescriptor> otherNodes;
    private Graph graph;
    private ArrayList<NodeDescriptor> neighbors;
    private ArrayList<NodeDescriptor> connectedNeighbors;
    private RoutingCache routingCache;


    // keep track of number of messages sent/receive by this node
    private int receiveTracker;
    private int sendTracker;
    private int relayTracker;

    // keep track of the sum of the random numbers sent
    private long sendSummation;
    private long receiveSummation;

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
            System.out.println(String.format("Messaging Node: %d started TCPReceiverThread", this.hostPort));
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        // Connect to Registry
        this.SendRegistrationRequest();

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
        } catch (NumberFormatException nfe)
        {
            System.out.println("Invalid Port Number for registry");
            return;
        }

        MessagingNode node = new MessagingNode(registryIPAddress, registryPort);

        ProcessInput(node);
    }

    private static void ProcessInput(MessagingNode node)
    {
        Scanner in = new Scanner(System.in);
        // TODO: After a run, exit-overlay doesn't work.
        while(true)
        {
            String input = in.nextLine();
            if (input.startsWith("connect"))
            {
                node.TestSendToNode(input);
                continue;
            }
            switch (input)
            {
                case "print-shortest-path":
                    System.out.println("Received Print Shortest Path command");
                    node.PrintShortestPath();
                    break;
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

    private void PrintShortestPath()
    {
        if (edges == null || edges.size() < 1)
        {
            System.out.println("Overlay not yet sent over.  Can't print shortest paths");
        } else
        {
            if (routingCache == null)
            {
                routingCache = new RoutingCache();
            }
            routingCache.fillAll(this.graph, new NodeDescriptor(this.hostIPAddress, this.hostPort), this.otherNodes);
            for (NodeDescriptor sink : this.otherNodes)
            {
                PrintNodeShortestPath(edges, routingCache.getPath(sink));
            }
        }

    }

    private void PrintNodeShortestPath(ArrayList<Edge> edges, ArrayList<NodeDescriptor> path)
    {
        for (int i = 0; i < path.size() - 1; i++)
        {
            System.out.print(path.get(i) + "--");
            for (Edge edge : edges)
            {
                if (edge.contains(path.get(i)) && edge.contains(path.get(i + 1)))
                {
                    System.out.print(edge.getWeight() + "--");
                }
            }
        }
        System.out.println(path.get(path.size() - 1));
    }

    private void TestSendToNode(String input)
    {
        int port = StringUtil.getIntFromStringCommand(input);
        ArrayList<NodeDescriptor> path = new ArrayList<>();
        path.add(new NodeDescriptor(this.hostIPAddress, this.hostPort));
        NodeDescriptor sink = new NodeDescriptor(this.hostIPAddress, port);

        Message message = new Message(new NodeDescriptor(this.hostIPAddress, this.hostPort),
                sink,
                12312,
                path);

        SendMessageToNode(message, sink);
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
            registrySocket.close();
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
            registrySocket.close();
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
        } else
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

    private synchronized void incrementRelayCounter()
    {
        this.relayTracker++;
    }

    private synchronized void addReceiveSummation(int value)
    {
        this.receiveSummation += value;
    }

    private synchronized void addSentSummation(int value)
    {
        this.sendSummation += value;
    }

    private synchronized void clearSentCounter()
    {
        this.sendTracker = 0;
    }

    private synchronized void clearReceivedCounter()
    {
        this.receiveTracker = 0;
    }

    private synchronized void clearRelayCounter()
    {
        this.relayTracker = 0;
    }

    private synchronized void clearReceivedSummation()
    {
        this.receiveSummation = 0;
    }

    private synchronized void clearSendSummation()
    {
        this.sendSummation = 0;
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
            //System.out.println("Received Message\nCurrent Receipts:" + this.receiveTracker);
            this.ReceiveMessage((Message) event);
        }
        if (event instanceof TaskInitiate)
        {
            this.InitiateTask((TaskInitiate) event);
        }
        if (event instanceof PullTrafficSummary)
        {
            this.PullTrafficSummary();
        }
        if (event instanceof MessagingNodesList)
        {
            this.ReceiveMessagingNodesList((MessagingNodesList) event);
        }
        if (event instanceof ConnectionRequest)
        {
            this.ReceiveConnectionRequest((ConnectionRequest) event);
        }
        if (event instanceof ConnectionResponse)
        {
            this.ReceiveConnectionResponse((ConnectionResponse) event);
        }
    }

    private void ReceiveConnectionResponse(ConnectionResponse event)
    {
        if (event.getStatus() == StatusCode.SUCCESS)
        {
            if (this.connectedNeighbors == null)
                this.connectedNeighbors = new ArrayList<>();
            this.connectedNeighbors.add(event.getResponseNode());
            System.out.println("Successfully connected to MessagingNode: " + event.getResponseNode().toString());

            if (this.connectedNeighbors.size() == this.neighbors.size())
                System.out.println("All connections are established.  Number of connections: " + this.connectedNeighbors.size());

        } else
        {
            System.out.println("Failed to connect to MessagingNode: " + event.getResponseNode().toString());
        }
    }

    private void ReceiveConnectionRequest(ConnectionRequest event)
    {
        NodeDescriptor requestor = event.getSourceNode();

        ConnectionResponse response = new ConnectionResponse(StatusCode.SUCCESS, new NodeDescriptor(this.hostIPAddress, this.hostPort));

        try
        {
            Socket responseSocket = new Socket(requestor.IPAddress, requestor.Port);
            TCPSender sender = new TCPSender(responseSocket);
            sender.sendData(response.getBytes());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void SendConnectionRequest(NodeDescriptor neighbor)
    {
        ConnectionRequest request = new ConnectionRequest(new NodeDescriptor(this.hostIPAddress, this.hostPort));

        try
        {
            Socket neighborSocket = new Socket(neighbor.IPAddress, neighbor.Port);
            TCPSender sender = new TCPSender(neighborSocket);
            sender.sendData(request.getBytes());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void ReceiveMessagingNodesList(MessagingNodesList message)
    {
        this.neighbors = new ArrayList<>(message.getNeighborNodes());
        for (NodeDescriptor neighbor : this.neighbors)
        {
            SendConnectionRequest(neighbor);
        }
    }

    private void PullTrafficSummary()
    {

        try
        {
            System.out.println("Sending TrafficSummary");
            TrafficSummary message = new TrafficSummary(this.hostIPAddress,
                    this.hostPort,
                    this.sendTracker,
                    this.sendSummation,
                    this.receiveTracker,
                    this.receiveSummation,
                    this.relayTracker);

            Socket registrySocket = new Socket(registryIPAddress, registryPort);
            TCPSender sender = new TCPSender(registrySocket);
            sender.sendData(message.getBytes());

            // Clear all counters
            clearSentCounter();
            clearReceivedCounter();
            clearRelayCounter();
            clearReceivedSummation();
            clearSendSummation();
            registrySocket.close();
        } catch (IOException ioe)
        {
            System.out.println("PullTrafficSummary: " + ioe.getMessage());
        }
    }

    private void SetOtherNodeList()
    {
        ArrayList<NodeDescriptor> otherNodes = new ArrayList<>();

        for (Edge edge : this.edges)
        {
            // we only want other nodes, not our self
            if (edge.getSource().IPAddress.equals(this.hostIPAddress) && edge.getSource().Port == this.hostPort)
                continue;

            // get a list of the other nodes we can use as sinks.
            if (!otherNodes.contains(edge.getSource()))
                otherNodes.add(edge.getSource());
        }
        this.otherNodes = otherNodes;
    }

    private void InitiateTask(TaskInitiate event)
    {
        int rounds = event.rounds;
        Random random = new Random();
        routingCache = new RoutingCache();

        for (int i = 0; i < rounds; i++)
        {
            int nodeNum = random.nextInt(this.otherNodes.size());
            SendMessage(this.otherNodes.get(nodeNum));
            try
            {
                Thread.sleep(100);
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        SendTaskComplete();
    }

    private void SendTaskComplete()
    {
        try
        {
            System.out.println("Sending Task Complete");
            TaskComplete message = new TaskComplete(this.hostIPAddress, this.hostPort);

            Socket registrySocket = new Socket(registryIPAddress, registryPort);
            TCPSender sender = new TCPSender(registrySocket);
            sender.sendData(message.getBytes());
            registrySocket.close();
        } catch (IOException ioe)
        {
            System.out.println("SendTaskComplete: " + ioe.getMessage());
        }
    }

    private synchronized void SendMessageToNode(Message message, NodeDescriptor node)
    {
        try
        {
            Socket nodeSocket = new Socket(node.IPAddress, node.Port);
            TCPSender sender = new TCPSender(nodeSocket);

            sender.sendData(message.getBytes());
            nodeSocket.close();
        } catch (IOException ioe)
        {
            System.out.println("SendMessageToNode: " + ioe.getMessage());
        }

    }

    private void SendMessage(NodeDescriptor sinkNode)
    {
        ArrayList<NodeDescriptor> nodePath;
        NodeDescriptor source = new NodeDescriptor(this.hostIPAddress, this.hostPort);

        if (routingCache.routeExists(sinkNode))
            nodePath = routingCache.getPath(sinkNode);
        else
        {
            DijkstraAlgorithm da = new DijkstraAlgorithm(this.graph);
            da.execute(source);
            nodePath = new ArrayList<>(da.getPath(sinkNode));
            routingCache.addPath(sinkNode, nodePath);
        }

        Random random = new Random();
        int payload = random.nextInt();
        Message message = new Message(source, sinkNode, payload, nodePath);
        this.incrementSentCounter();
        this.addSentSummation(message.getPayload());

        // Send to the first item in the path list
        this.SendMessageToNode(message, nodePath.get(1));

    }

    private void ReceiveMessage(Message event)
    {
        ArrayList<NodeDescriptor> route = event.getRoutingPath();
        NodeDescriptor me = new NodeDescriptor(0, this.hostIPAddress, this.hostPort);
        if (event.getDestination().equals(me))
        {
            // We've reached the destination!
            //System.out.println("Message received destination");
            this.incrementReceivedCounter();
            this.addReceiveSummation(event.getPayload());
            return;
        }
        // Find the next node in the routing path
        ListIterator<NodeDescriptor> node = route.listIterator();
        NodeDescriptor nextNode = null;
        while (node.hasNext())
        {
            NodeDescriptor current = node.next();
            if (current.equals(me))
            {
                nextNode = node.next();
                //System.out.println("Message received, routing to " + nextNode);
                break;
            }
        }
        if (nextNode != null)
        {
            this.incrementRelayCounter();
            //System.out.println("increment relay counter");
            this.SendMessageToNode(event, nextNode);
        }
    }

    private void ReceiveLinkWeights(LinkWeights event)
    {
        this.edges = event.nodeWeights;
        this.graph = new Graph(event.nodeWeights);
        SetOtherNodeList();

        System.out.println("Link Weights received and processed.  Ready to send messages.");
        //System.out.println(this.edges.toString());
    }
}
