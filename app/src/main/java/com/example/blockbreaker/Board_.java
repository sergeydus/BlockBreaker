
package com.example.blockbreaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board_ {

    public Player1 player1;
    public Float player1score;
    public Float player2score;
    public Player2 player2;
    public List<Block> blocks = null;
    public List<Ball> balls = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
