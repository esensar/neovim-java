package com.ensarsarajcic.neovim.java.pluginhost.opts;

import com.ensarsarajcic.neovim.java.pluginhost.annotations.NeovimAutocommand;

import java.util.HashMap;
import java.util.Map;

public record AutocommandOpts(
        String group,
        String pattern,
        String description,
        boolean once,
        boolean nested,
        boolean sync
) {

    public static AutocommandOpts fromAnnotation(NeovimAutocommand autocommand) {
        return new AutocommandOpts(
                autocommand.group(),
                autocommand.pattern(),
                autocommand.description(),
                autocommand.once(),
                autocommand.nested(),
                autocommand.sync()
        );
    }

    public Map<String, Object> toNeovimOptions() {
        var map = new HashMap<String, Object>();
        map.put("group", group);
        map.put("pattern", pattern);
        map.put("once", once);
        map.put("nested", nested);
        map.put("desc", description);
        return map;
    }
}
