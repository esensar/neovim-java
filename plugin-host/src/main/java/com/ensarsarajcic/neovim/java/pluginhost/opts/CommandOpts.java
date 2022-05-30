package com.ensarsarajcic.neovim.java.pluginhost.opts;

import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimCommand;

import java.util.HashMap;
import java.util.Map;

public record CommandOpts(
        String nargs,
        String complete,
        String description,
        boolean enableRange,
        String range,
        String count,
        String addr,
        boolean force,
        boolean bang,
        boolean bar,
        boolean register,
        boolean keepScript,
        boolean sync
) {

    public static CommandOpts fromAnnotation(NeovimCommand command) {
        return new CommandOpts(
                command.nargs(),
                command.complete(),
                command.description(),
                command.enableRange(),
                command.range(),
                command.count(),
                command.addr(),
                command.force(),
                command.bang(),
                command.bar(),
                command.register(),
                command.keepScript(),
                command.sync()
        );
    }

    public Map<String, Object> toNeovimOptions() {
        var map = new HashMap<String, Object>();
        try {
            var nargsAsInt = Integer.parseInt(nargs);
            map.put("nargs", nargsAsInt);
        } catch (NumberFormatException ex) {
            map.put("nargs", nargs);
        }
        if (force) {
            map.put("force", true);
        }
        if (bang) {
            map.put("bang", true);
        }
        if (register) {
            map.put("register", true);
        }
        if (keepScript) {
            map.put("keepscript", true);
        }
        if (!description.isBlank()) {
            map.put("desc", description);
        }
        if (enableRange && !range.isBlank()) {
            map.put("range", range);
        }
        if (!count.isBlank()) {
            map.put("count", count);
        }
        if (!addr.isBlank()) {
            map.put("addr", addr);
        }
        return map;
    }
}
