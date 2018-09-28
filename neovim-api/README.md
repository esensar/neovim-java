# neovim-api

This is the main high level interface for this library. It provides all of the Neovim RPC API functions in an easy to use way. It holds all types as models
and provides 4 separate **APIs**: *Neovim*, *Buffer*, *Tabpage* and *Window* (the way they were meant to be used in OOP languages).

> This does not proivide atomic calls yet.

All calls are made using *reactive-core-rpc* module and all calls return `CompletableFuture`. If you prefer RxJava over Java 9 Flows, you can use
*neovim-rx-api* module.

Example usage:
```java
    Socket socket = new Socket("127.0.0.1", 1234);
    
    RPCConnection localConnection = new TcpSocketRPCConnection(socket);
    NeovimApi api = NeovimApis.getApiForConnection(localConnection);
    
    api.getCurrentLine().thenAccept(System.out::println);
    
    NeovimApi another = new NeovimStreamApi(ReactiveRPCClient.getDefaultInstance());
```