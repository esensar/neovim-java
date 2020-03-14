package com.ensarsarajcic.neovim.java.rpluginex;

import com.ensarsarajcic.neovim.java.corerpc.client.RpcClient;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcListener;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcStreamer;
import com.ensarsarajcic.neovim.java.corerpc.client.StdIoRpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RpcError;
import com.ensarsarajcic.neovim.java.handler.NeovimHandlerManager;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimRequestHandler;

import java.io.IOException;

public final class RPluginExample {
    public static void main(String[] args) {
        var rpcConnection = new StdIoRpcConnection();
        var streamer = RpcClient.getDefaultAsyncInstance();
        NeovimHandlerManager neovimHandlerManager = new NeovimHandlerManager();
        neovimHandlerManager.registerNeovimHandler(new Limit(streamer));
        neovimHandlerManager.attachToStream(streamer);
        streamer.attach(rpcConnection);
    }

    /**
     * Example of remote plugin from https://neovim.io/doc/user/remote_plugin.html
     * It simply limits number of requests made to it
     */
    public static final class Limit {
        private int callCount = 0;
        private final RpcStreamer rpcStreamer;

        public Limit(RpcStreamer rpcStreamer) {
            this.rpcStreamer = rpcStreamer;
        }

        @NeovimRequestHandler("increment_calls")
        public void incrementCalls(RequestMessage requestMessage) throws IOException {
            if (callCount == 5) {
                rpcStreamer.send(
                        new ResponseMessage(requestMessage.getId(), new RpcError(0, "Too many calls!"), null)
                );
                return;
            }
            callCount++;
            rpcStreamer.send(
                    new ResponseMessage(requestMessage.getId(), null, "Count: " + callCount)
            );
        }
    }
}
