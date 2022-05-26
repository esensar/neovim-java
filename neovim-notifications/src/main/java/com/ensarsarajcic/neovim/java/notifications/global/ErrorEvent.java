package com.ensarsarajcic.neovim.java.notifications.global;

import com.ensarsarajcic.neovim.java.corerpc.message.RpcError;

import java.util.List;
import java.util.function.Function;

public final class ErrorEvent implements GlobalEvent {
    public static final String NAME = "nvim_error_event";

    public static final Function<List, ErrorEvent> CREATOR = list -> new ErrorEvent(
            new RpcError(
                    (Integer) list.get(0),
                    (String) list.get(1)
            )
    );

    private final RpcError error;

    public ErrorEvent(RpcError error) {
        this.error = error;
    }

    public RpcError getError() {
        return error;
    }

    @Override
    public String getNotificationName() {
        return NAME;
    }
}
