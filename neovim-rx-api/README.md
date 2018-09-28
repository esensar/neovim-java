# neovim-rx-api

Neovim RX api module provides a same API as neovim API module, but using RxJava2.

Example:
```java
    Socket socket = new Socket("127.0.0.1", 1234);
    
    RPCConnection localConnection = new TcpSocketRPCConnection(socket);
    NeovimApi api = NeovimApis.getApiForConnection(localConnection); // Create regular API
    NeovimRxApi rxApi = new NeovimRxWrapper(api); // And then wrap it in RxJava2 interface
    
    api.getCurrentLine().thenAccept(System.out::println);
    rxApi.getCurrentLine().subscribe(System.out::println);
```