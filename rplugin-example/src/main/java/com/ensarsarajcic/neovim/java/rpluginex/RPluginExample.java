package com.ensarsarajcic.neovim.java.rpluginex;

import com.ensarsarajcic.neovim.java.handler.errors.NeovimRequestException;
import com.ensarsarajcic.neovim.java.pluginhost.NeovimJavaPluginHost;
import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimCommand;
import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimJavaHostedPlugin;
import com.ensarsarajcic.neovim.java.pluginhost.state.CommandState;

public final class RPluginExample {
    private RPluginExample() {
        // no instance
    }

    public static void main(String[] args) {
        var host = new NeovimJavaPluginHost();
        // Start the plugin host
        host.start(args);
    }

    /**
     * Example of remote plugin from https://neovim.io/doc/user/remote_plugin.html
     * It simply limits number of requests made to it
     */
    @NeovimJavaHostedPlugin
    public static final class Limit {
        private int callCount = 0;

        @NeovimCommand(value = "NeovimJavaIncrementCalls", sync = true)
        public String incrementCalls(CommandState state)
                throws NeovimRequestException {
            if (callCount == 5) {
                throw new NeovimRequestException("Too many calls!");
            }
            callCount++;
            return "Count: " + callCount + " " + state.toString();
        }
    }
}
