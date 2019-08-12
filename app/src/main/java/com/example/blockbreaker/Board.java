
package com.example.blockbreaker;

import java.util.HashMap;
import java.util.Map;

public class Board {

    public Board_ board;
    public Boolean isplayer1;
    public Boolean isover;
    public Boolean HasWon;
    public String opponent;
    public Float message;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
