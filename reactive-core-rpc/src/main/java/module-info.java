module com.ensarsarajcic.neovim.java.reactivecorerpc {
    opens com.ensarsarajcic.neovim.java.corerpc.reactive;
    exports com.ensarsarajcic.neovim.java.corerpc.reactive;

    requires com.ensarsarajcic.neovim.java.corerpc;
    requires org.slf4j;
}