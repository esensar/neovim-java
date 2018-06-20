package com.ensarsarajcic.neovim.java;

import com.ensarsarajcic.neovim.java.api.NeovimStreamApi;
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimJacksonModule;
import com.ensarsarajcic.neovim.java.corerpc.client.ProcessRPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RPCClient;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCClient;
import com.ensarsarajcic.neovim.java.rxapi.*;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");


        // Create a default instance
        ProcessBuilder pb = new ProcessBuilder("nvim", "--embed");
        Process neovim = pb.start();

        RPCClient rpcClient = new RPCClient.Builder()
                .withObjectMapper(NeovimJacksonModule.createNeovimObjectMapper()).build();
        rpcClient.attach(new ProcessRPCConnection(neovim));

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
