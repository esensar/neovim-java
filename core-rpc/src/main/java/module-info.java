module com.ensarsarajcic.neovim.java.corerpc {
    opens com.ensarsarajcic.neovim.java.corerpc.client;
    opens com.ensarsarajcic.neovim.java.corerpc.message;
    exports com.ensarsarajcic.neovim.java.corerpc.client;
    exports com.ensarsarajcic.neovim.java.corerpc.message;

    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires jackson.dataformat.msgpack;
}