module com.ensarsarajcic.neovim.java.pluginhost {
    exports com.ensarsarajcic.neovim.java.pluginhost.annotations;
    exports com.ensarsarajcic.neovim.java.pluginhost;
    exports com.ensarsarajcic.neovim.java.pluginhost.state;
    exports com.ensarsarajcic.neovim.java.pluginhost.opts;
    requires com.ensarsarajcic.neovim.java.corerpc;
    requires com.ensarsarajcic.neovim.java.handlerannotations;
    requires com.ensarsarajcic.neovim.java.neovimapi;
    requires com.ensarsarajcic.neovim.java.reactivecorerpc;
    requires neovim.notifications;
    requires org.reflections;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
}