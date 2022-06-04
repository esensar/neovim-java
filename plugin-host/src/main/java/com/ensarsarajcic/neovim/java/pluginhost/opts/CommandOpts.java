/*
 * MIT License
 *
 * Copyright (c) 2022 Ensar Sarajčić
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
