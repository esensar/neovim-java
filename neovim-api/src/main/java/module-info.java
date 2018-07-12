module neovimapi {
    exports com.ensarsarajcic.neovim.java.api;
    exports com.ensarsarajcic.neovim.java.api.buffer;
    exports com.ensarsarajcic.neovim.java.api.tabpage;
    exports com.ensarsarajcic.neovim.java.api.window;
    exports com.ensarsarajcic.neovim.java.api.types.api;
    exports com.ensarsarajcic.neovim.java.api.types.msgpack;
    exports com.ensarsarajcic.neovim.java.api.types.apiinfo;

    requires corerpc;
    requires reactivecorerpc;
    requires jackson.annotations;
    requires msgpack.core;
    requires jackson.core;
    requires jackson.databind;
    requires jackson.dataformat.msgpack;
}