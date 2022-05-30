package com.ensarsarajcic.neovim.java.rpluginex;

import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimRequestHandler;
import com.ensarsarajcic.neovim.java.handler.errors.NeovimRequestException;
import com.ensarsarajcic.neovim.java.pluginhost.NeovimJavaPluginHost;

public final class RPluginExample {
    private RPluginExample() {
        // no instance
    }

    public static void main(String[] args) {
        var host = new NeovimJavaPluginHost();
        // Start the plugin host
        host.start(args).thenAccept(unused -> {
            // After plugin host has been started, API and handlers can be accessed
            // Register handler for `Limit` functionality
            host.getNeovimHandlerManager().registerNeovimHandler(new Limit());
            // Get high-level plugin API, to easily define custom commands
            // Create a request command - command which will use result of the request and echo it
            host.getPluginApi().addRequestCommand(
                    "NeovimJavaIncrementCount",
                    "increment_calls",
                    true,
                    "Increment the counter up to a defined limit. Throws error if over the limit"
            );
        });
    }

    /**
     * Example of remote plugin from https://neovim.io/doc/user/remote_plugin.html
     * It simply limits number of requests made to it
     */
    public static final class Limit {
        private int callCount = 0;

        @NeovimRequestHandler("increment_calls")
        public String incrementCalls(RequestMessage requestMessage)
                throws NeovimRequestException {
            if (callCount == 5) {
                throw new NeovimRequestException("Too many calls!");
            }
            callCount++;
            return "Count: " + callCount;
        }
    }
}
