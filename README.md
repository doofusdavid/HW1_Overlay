# CS455 - Intro to Distributed Systems #

__David Edwards__

*david.edwards@colostate.edu*

### Notes ###
I'm hoping most things are laid out as presented in the assignment.  I may have erred on
the side of too much output.  

1. Because I'm a remote student, and 
was unable to work in the lab, I did most of my tests on my Mac.  I verified that the code
worked by opening 10 windows, ssh-ed into various CS computers to run the MessagingNode 
and Registry
2. Despite attempts, the tabs for the Traffic Summary don't line up perfectly.  My machine is
stuck on 4 spaces per tab, the CS ones on 8.  It'd probably be preferable to output it as CSV or
something that some other software could work with.

## cs455.dijkstra ##
- __DijkstraAlgorithm__

   Contains the implementation of Dijkstra's Algorithm I used to create the shortest path between MessagingNodes

- __Edge__

   Represents an edge in the graph.  Consists of two NodeDescriptors and a weight, representing the weight of the connection.

- __Graph__

   Implementation of the weighted graph containing nodes and edges.  Passed to Dijkstra's algorithm, and also can generate a list of Edges which are passed down to the MessagingNodes from the Registry

- __NodeDescriptor__

   The basic node descriptor used for nodes all throughout the project.  Contains an IP address and a Port.  Used as a message component, in a weighted graph, and elsewhere.

- __RoutingCache__

   Rather than have the MessagingNode calculate the shortest path every time it sent a message, we only calculate once per sink node, and store here for easy retrieval

## cs455.node ##
- __MessagingNode__

   Responsible for registering self at the registry, receiving information from the registry, communicating with other MessagingNodes, collecting statistics, and sending to the Registry.

- __Node__

   The interface used by MessagingNode and Registry.

- __Registry__

   Responsible for accepting registrations from MessagingNodes, generating the graph of all nodes, sending those weights to nodes, initiating the message-sending-task, collecting and displaying data returned.

## cs455.transport ##
- __TCPReceiverThread__

   Contains the logic for receiving data from other MessagingNodes/Registry.  Upon receipt, calls the EventFactory.  Started as a thread in both MessagingNode and Registry.

- __TCPSender__
   
   Responsible for sending data from MessagingNodes/Registry.
   
## cs455.util ##

- __IPChecker__

   String regex to ensure that passed-in IP addresses are valid.

- __NotImplementedException__

   I come from a .NET background, where I would throw one of these for every new, not-yet-implemented method.  By the time the project is complete, they should all be gone.

- __StringUtil__

   Used for parsing command line input.

## cs455.wireformats ##

Most wireformat classes consist of messages sent back and forth from MessagingNode to Registry, or MessagingNode to MessagingNode.

- __ConnectionRequest__

   When the MessagingNodesList is sent to a MessagingNode, this connection is sent to all connected nodes to establish a connection.

- __ConnectionResponse__

   When a ConnectionRequest is received by a neighbor node, they will send back a ConnectionResponse

- __DeregisterRequest__

   When a node tears down, it must deregister itself from the Registry using this format

- __DeregisterResponse__

   A messaging node responds to the registry with this message

- __Event__

   Interface containing the shared methods used by all Events

- __EventFactory__

  EventFactory is called the TCPThreadReceiver, parses the message, and calls a Node's onEvent method with the send Event.

- __LinkWeights__

   Contains all of the Edges, which are sent to each MessagingNode.  Used to rehydrate a graph, which can then be fed into DijkstraAlgorithm to calculate shortest paths.

- __Message__

   The Message sent back and forth between MessagingNode.  Contains an int payload, source, destination, and routing path.

- __MessageType__

   Constant list of messaging types used to differentiate between the various Events.

- __MessagingNodesList__

   List of neighbor nodes sent to a MessagingNode

- __PullTrafficSummary__

   Request from the Registry to send all traffic statistics back to the Registry

- __RegisterRequest__

   When a MessagingNode is created, it sends a RegisterRequest to the Registry to announce its presence.

- __RegisterResponse__

   After a MessagingNode registers itself, the Registry sends back an acknowledgement.

- __StatusCode__

   Some Events have a SUCCESS or FAILURE, which are defined here.

- __TaskComplete__

   When a MessagingNode has sent its Messages to other MessagingNodes the appropriate number of times, it informs the registry it is done.

- __TaskInitiate__

   Message from the Registry telling a MessagingNode to start the Message sending

- __TrafficSummary__

   Sent by MessagingNodes to the Registry with their sent, received and relayed statistics.
   