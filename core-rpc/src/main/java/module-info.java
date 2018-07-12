module corerpc {
    opens com.ensarsarajcic.neovim.java.corerpc.client;
    opens com.ensarsarajcic.neovim.java.corerpc.message;
    exports com.ensarsarajcic.neovim.java.corerpc.client;
    exports com.ensarsarajcic.neovim.java.corerpc.message;

    requires jackson.databind;
    requires slf4j.api;
    requires jackson.core;
    requires jackson.dataformat.msgpack;
    requires jackson.annotations;
}