CS455 - Intro to Distributed Systems
David Edwards
david.edwards@colostate.edu

See README.md for formatted version

CS455 - Intro to Distributed Systems
David Edwards

david.edwards@colostate.edu

Notes

I'm hoping most things are laid out as presented in the assignment. I may have erred on the side of too much output.

Because I'm a remote student, and was unable to work in the lab, I did most of my tests on my Mac. I verified that the code worked by opening 10 windows, ssh-ed into various CS computers to run the MessagingNode and Registry
Despite attempts, the tabs for the Traffic Summary don't line up perfectly. My machine is stuck on 4 spaces per tab, the CS ones on 8. It'd probably be preferable to output it as CSV or something that some other software could work with.
cs455.dijkstra

DijkstraAlgorithm

Contains the implementation of Dijkstra's Algorithm I used to create the shortest path between MessagingNodes

Edge

Represents an edge in the graph. Consists of two NodeDescriptors and a weight, representing the weight of the connection.

Graph

Implementation of the weighted graph containing nodes and edges. Passed to Dijkstra's algorithm, and also can generate a list of Edges which are passed down to the MessagingNodes from the Registry

NodeDescriptor

The basic node descriptor used for nodes all throughout the project. Contains an IP address and a Port. Used as a message component, in a weighted graph, and elsewhere.

RoutingCache

Rather than have the MessagingNode calculate the shortest path every time it sent a message, we only calculate once per sink node, and store here for easy retrieval

cs455.node

MessagingNode

Responsible for registering self at the registry, receiving information from the registry, communicating with other MessagingNodes, collecting statistics, and sending to the Registry.

Node

The interface used by MessagingNode and Registry.

Registry

Responsible for accepting registrations from MessagingNodes, generating the graph of all nodes, sending those weights to nodes, initiating the message-sending-task, collecting and displaying data returned.

cs455.transport

TCPReceiverThread

Contains the logic for receiving data from other MessagingNodes/Registry. Upon receipt, calls the EventFactory. Started as a thread in both MessagingNode and Registry.

TCPSender

Responsible for sending data from MessagingNodes/Registry.

cs455.util

IPChecker

String regex to ensure that passed-in IP addresses are valid.

NotImplementedException

I come from a .NET background, where I would throw one of these for every new, not-yet-implemented method. By the time the project is complete, they should all be gone.

StringUtil

Used for parsing command line input.

cs455.wireformats

Most wireformat classes consist of messages sent back and forth from MessagingNode to Registry, or MessagingNode to MessagingNode.

ConnectionRequest

When the MessagingNodesList is sent to a MessagingNode, this connection is sent to all connected nodes to establish a connection.

ConnectionResponse

When a ConnectionRequest is received by a neighbor node, they will send back a ConnectionResponse

DeregisterRequest

When a node tears down, it must deregister itself from the Registry using this format

DeregisterResponse

The Registry responds to the requesting MessagingNode with this Event

Event

Interface containing the shared methods used by all Events

EventFactory

EventFactory is called the TCPThreadReceiver, parses the message, and calls a Node's onEvent method with the send Event.

LinkWeights

Contains all of the Edges, which are sent to each MessagingNode. Used to rehydrate a graph, which can then be fed into DijkstraAlgorithm to calculate shortest paths.

Message

The Message sent back and forth between MessagingNode. Contains an int payload, source, destination, and routing path.

MessageType

Constant list of messaging types used to differentiate between the various Events.

MessagingNodesList

List of neighbor nodes sent to a MessagingNode

PullTrafficSummary

Request from the Registry to send all traffic statistics back to the Registry

RegisterRequest

When a MessagingNode is created, it sends a RegisterRequest to the Registry to announce its presence.

RegisterResponse

After a MessagingNode registers itself, the Registry sends back an acknowledgement.

StatusCode

Some Events have a SUCCESS or FAILURE, which are defined here.

TaskComplete

When a MessagingNode has sent its Messages to other MessagingNodes the appropriate number of times, it informs the registry it is done.

TaskInitiate

Message from the Registry telling a MessagingNode to start the Message sending

TrafficSummary

Sent by MessagingNodes to the Registry with their sent, received and relayed statistics.
