package com.ensarsarajcic.neovim.java.explorer.api.discovery;

import com.ensarsarajcic.neovim.java.explorer.api.NeovimApiList;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ApiDiscovery {

    private static List<String> createArgs(String executable) {
        List<String> allArgs = new ArrayList<>(2);
        allArgs.add(executable);
        allArgs.add("--api-info");
        return allArgs;
    }

    public static NeovimApiList discoverApi() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(createArgs("nvim"));
        Process neovim = pb.start();
        MessagePackFactory factory = new MessagePackFactory();
        factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        return new ObjectMapper(factory).readerFor(NeovimApiList.class).readValue(neovim.getInputStream());
    }
}
