package com.ensarsarajcic.neovim.java.rpluginex;

import com.ensarsarajcic.neovim.java.corerpc.client.RpcClient;
import com.ensarsarajcic.neovim.java.corerpc.client.StdIoRpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.handler.NeovimHandlerManager;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimRequestHandler;
import com.ensarsarajcic.neovim.java.handler.errors.NeovimRequestException;

import java.io.IOException;

public final class RPluginExample {
    private RPluginExample() {
        //no instance
    }

    public static void main(String[] args) throws IOException {
        var rpcConnection = new StdIoRpcConnection();
        var streamer = RpcClient.getDefaultAsyncInstance();
        NeovimHandlerManager neovimHandlerManager = new NeovimHandlerManager();
        neovimHandlerManager.registerNeovimHandler(new Limit());
        neovimHandlerManager.attachToStream(streamer);
        streamer.attach(rpcConnection);
    }

    /**
     * Example of remote plugin from https://neovim.io/doc/user/remote_plugin.html
     * It simply limits number of requests made to it
     */
    public static final class Limit {
        private int callCount = 0;

        @NeovimRequestHandler("increment_calls")
        public String incrementCalls(RequestMessage requestMessage) throws NeovimRequestException {
            if (callCount == 5) {
                throw new NeovimRequestException("Too many calls!");
            }
            callCount++;
            return "Count: " + callCount;
        }
    }
}
