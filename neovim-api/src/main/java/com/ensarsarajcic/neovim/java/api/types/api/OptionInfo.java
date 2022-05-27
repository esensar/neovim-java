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

package com.ensarsarajcic.neovim.java.api.types.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class OptionInfo {

    @JsonProperty("name")
    private final String name;
    @JsonProperty("shortname")
    private final String shortName;
    @JsonProperty("type")
    private final String type;
    @JsonProperty("default")
    private final String defaultValue;
    @JsonProperty("was_set")
    private final boolean wasSet;
    @JsonProperty("last_set_sid")
    private final String lastSetSId;
    @JsonProperty("last_set_linenr")
    private final int lastSetLineNr;
    @JsonProperty("last_set_chan")
    private final int lastSetChan;
    @JsonProperty("scope")
    private final String scope;
    @JsonProperty("global_local")
    private final boolean globalLocal;
    @JsonProperty("commalist")
    private final List<String> commaList;
    @JsonProperty("flaglist")
    private final List<Character> flagList;

    public OptionInfo(String name,
                      String shortName,
                      String type,
                      String defaultValue,
                      boolean wasSet,
                      String lastSetSId,
                      int lastSetLineNr,
                      int lastSetChan,
                      String scope,
                      boolean globalLocal,
                      List<String> commaList,
                      List<Character> flagList) {
        this.name = name;
        this.shortName = shortName;
        this.type = type;
        this.defaultValue = defaultValue;
        this.wasSet = wasSet;
        this.lastSetSId = lastSetSId;
        this.lastSetLineNr = lastSetLineNr;
        this.lastSetChan = lastSetChan;
        this.scope = scope;
        this.globalLocal = globalLocal;
        this.commaList = commaList;
        this.flagList = flagList;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isWasSet() {
        return wasSet;
    }

    public String getLastSetSId() {
        return lastSetSId;
    }

    public int getLastSetLineNr() {
        return lastSetLineNr;
    }

    public int getLastSetChan() {
        return lastSetChan;
    }

    public String getScope() {
        return scope;
    }

    public boolean isGlobalLocal() {
        return globalLocal;
    }

    public List<String> getCommaList() {
        return commaList;
    }

    public List<Character> getFlagList() {
        return flagList;
    }
}
