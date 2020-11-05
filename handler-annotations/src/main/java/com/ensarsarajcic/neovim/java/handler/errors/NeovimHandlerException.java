package com.ensarsarajcic.neovim.java.handler.errors;

import com.ensarsarajcic.neovim.java.corerpc.message.RpcError;

public class NeovimHandlerException extends Exception {
    public NeovimHandlerException(String message) {
        super(message);
    }

    public RpcError toRpcError() {
        return RpcError.exception(getMessage());
    }
}
