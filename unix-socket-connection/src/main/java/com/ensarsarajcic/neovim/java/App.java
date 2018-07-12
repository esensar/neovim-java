package com.ensarsarajcic.neovim.java;

import com.ensarsarajcic.neovim.java.corerpc.client.RPCClient;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.unix.socket.UnixDomainSocketRPCConnection;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {

        UnixDomainSocketRPCConnection unixDomainSocketRPCConnection = new UnixDomainSocketRPCConnection(new File("/var/folders/zl/q6317czn2b5d4g53prd0ncl80000gn/T/nvimUeZsPh/0"));

        RPCClient rpcClient = RPCClient.getDefaultAsyncInstance();

        rpcClient.attach(unixDomainSocketRPCConnection);

        rpcClient.send(
                new RequestMessage.Builder("nvim_ui_attach")
                        .addArgument(300)
                        .addArgument(300)
                        .addArgument(Collections.emptyList())
        );

        rpcClient.addNotificationCallback(System.out::println);

        System.out.println("Hello World!");
    }
}
