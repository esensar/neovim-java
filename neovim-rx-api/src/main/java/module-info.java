module com.ensarsarajcic.neovim.java.neovimrxapi {
    exports com.ensarsarajcic.neovim.java.rxapi;
    opens com.ensarsarajcic.neovim.java.rxapi;

    requires com.ensarsarajcic.neovim.java.neovimapi;
    requires io.reactivex.rxjava2;
    requires com.ensarsarajcic.neovim.java.corerpc;
}