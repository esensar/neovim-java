module com.ensarsarajcic.neovim.java.unixsocketconnection {
    exports com.ensarsarajcic.neovim.java.unix.socket;

    requires com.ensarsarajcic.neovim.java.corerpc;
    requires ipcsocket;
    requires org.slf4j;
}