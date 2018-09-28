# unix-socket-connection

Unix socket connection module provides a very simple additional implementation of `RPCConnection` based on Unix domain sockets.

Example usage:
```java
    File socket = new File("/var/nvim/random");

    RPCConnection fileConnection = new UnixDomainSocketRPCConnection(socket);

    // It can now be used for communication
    rpcStreamer.attach(fileConnection);
    rpcStreamer.sent(message); // send a message to unix domain socket located on /var/nvim/random
```