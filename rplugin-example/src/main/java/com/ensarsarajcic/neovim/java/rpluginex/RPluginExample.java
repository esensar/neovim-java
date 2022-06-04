package com.ensarsarajcic.neovim.java.rpluginex;

import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimRequestHandler;
import com.ensarsarajcic.neovim.java.handler.errors.NeovimRequestException;
import com.ensarsarajcic.neovim.java.pluginhost.NeovimJavaPluginHost;
import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimAutocommand;
import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimCommand;
import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimJavaHostedPlugin;
import com.ensarsarajcic.neovim.java.pluginhost.state.AutocommandState;
import com.ensarsarajcic.neovim.java.pluginhost.state.CommandState;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public final class RPluginExample {
    private RPluginExample() {
        // no instance
    }

    public static void main(String[] args) {
        var host = new NeovimJavaPluginHost();
        // Start the plugin host
        host.start(args);

        // directly use the host here, or create hosted plugin with annotations
        host.getApi().executeCommand("echo hello");

        // or add RPC request/notification handlers manually
        host.getNeovimHandlerManager().registerNeovimHandler(new CustomHandler());
    }

    /**
     * Example of remote plugin from https://neovim.io/doc/user/remote_plugin.html
     * It simply limits number of requests made to it
     */
    @NeovimJavaHostedPlugin
    public static final class Limit {
        private int callCount = 0;
        private final NeovimJavaPluginHost host;

        // Optionally declare constructor to take plugin host, for use in other methods
        public Limit(NeovimJavaPluginHost host) {
            this.host = host;
        }

        // add preparation hook - called before registering commands and autocommands
        // called before any hosted plugin is prepared
        // useful to add autocommand group for example
        public CompletableFuture<Void> prepare() {
            return host.getApi().createAugroup("RPluginExampleGroup", new HashMap<>()).thenApply(i -> null);
        }

        // add ready hook - called after all hosted plugins are prepared
        // and all of their commands and autocommands were registered
        // at this point it is safe to make calls that may require these to be set up
        // it is also possible to not return CompletableFuture from these hooks
        // result is ignored then
        public void onReady() {
        }

        @NeovimCommand(value = "NeovimJavaIncrementCalls", sync = true)
        public String incrementCalls(CommandState state)
                throws NeovimRequestException {
            System.err.println("Command: " + state);
            if (callCount == 5) {
                throw new NeovimRequestException("Too many calls!");
            }
            callCount++;
            return "Count: " + callCount + " " + state.toString();
        }

        @NeovimAutocommand(value = {"BufRead"}, pattern = "*", group = "RPluginExampleGroup")
        public void handleBufRead(AutocommandState autocommandState) {
            System.err.println("Autocommand: " + autocommandState);
        }
    }

    public static class CustomHandler {
        // This method can be called from neovim with `rpcrequest(<id>, "return_string")`
        @NeovimRequestHandler("return_string")
        public String returnString(RequestMessage message) {
            return "String from CustomHandler";
        }
    }
}
