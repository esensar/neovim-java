package com.ensarsarajcic.neovim.java.commonhost;

import com.ensarsarajcic.neovim.java.pluginhost.NeovimJavaPluginHost;

public final class CommonPluginHost {
    private CommonPluginHost() {
        // no instance
    }

    public static void main(String[] args) {
        var host = new NeovimJavaPluginHost();
        // Start the common plugin host
        host.start(args);
    }
}
