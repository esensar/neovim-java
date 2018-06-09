package com.ensarsarajcic.neovim.java;

import com.ensarsarajcic.neovim.java.api.NeovimStreamApi;
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimJacksonModule;
import com.ensarsarajcic.neovim.java.corerpc.client.RPCClient;
import com.ensarsarajcic.neovim.java.corerpc.client.TcpSocketRPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCClient;
import com.ensarsarajcic.neovim.java.rxapi.*;
import io.reactivex.SingleTransformer;

import java.io.IOException;
import java.net.Socket;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");


        // Create a default instance
        Socket socket = new Socket("127.0.0.1", 6666);

        RPCClient rpcClient = new RPCClient.Builder()
                .withObjectMapper(NeovimJacksonModule.createNeovimObjectMapper()).build();
        rpcClient.attach(new TcpSocketRPCConnection(socket));

        NeovimStreamApi neovimStreamApi = new NeovimStreamApi(
                ReactiveRPCClient.createDefaultInstanceWithCustomStreamer(rpcClient)
        );

        NeovimRxApi neovimRxApi = new NeovimRxWrapper(neovimStreamApi);

        neovimRxApi.input("jjj").subscribe(
                System.out::println,
                System.out::println
        );

        neovimRxApi.getCurrentBuffer()
                .flatMap(NeovimBufferRxApi::getLineCount)
                .subscribe(
                        System.out::println,
                        System.out::println
                );

        neovimRxApi.getCurrentTabpage()
                .flatMap(NeovimTabpageRxApi::getWindow)
                .flatMap(NeovimWindowRxApi::getTabpage)
                .flatMap(NeovimTabpageRxApi::getWindow)
                .flatMap(NeovimWindowRxApi::getCursor)
                .subscribe(
                        System.out::println,
                        System.out::println
                );
    }
}
