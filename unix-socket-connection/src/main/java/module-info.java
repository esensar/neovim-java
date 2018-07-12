module unixsocketconnection {
    exports com.ensarsarajcic.neovim.java.unix.socket;

    requires corerpc;
    requires ipcsocket;
    requires slf4j.api;
}