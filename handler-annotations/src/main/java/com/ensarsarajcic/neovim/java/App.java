package com.ensarsarajcic.neovim.java;

import com.ensarsarajcic.neovim.java.corerpc.client.ProcessRPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RPCClient;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.handler.NeovimHandlerManager;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimNotificationHandler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );

        // Create a default instance
        ProcessBuilder pb = new ProcessBuilder("nvim", "--embed");
        Process neovim = pb.start();

        RPCClient rpcClient = new RPCClient.Builder().build();
        rpcClient.attach(new ProcessRPCConnection(neovim));

        NeovimHandlerManager neovimHandlerManager = new NeovimHandlerManager();
        neovimHandlerManager.attachToStream(rpcClient);
        neovimHandlerManager.registerNeovimHandler(new Handler());

        rpcClient.send(new RequestMessage.Builder("nvim_ui_attach").addArgument(300).addArgument(300).addArgument(new ArrayList<>()));
    }

    public static class Handler {
        @NeovimNotificationHandler("redraw")
        public void onNotification(NotificationMessage notificationMessage) {
            System.out.println("Received message on handler: " + notificationMessage);
        }
    }
}
