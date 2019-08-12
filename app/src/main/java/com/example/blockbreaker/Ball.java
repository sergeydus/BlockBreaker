
package com.example.blockbreaker;

import java.util.HashMap;
import java.util.Map;

public class Ball {

    public Float score;
    public Float height;
    public Float width;
    public Float posx;
    public Float posy;
    public Float dx;
    public Float dy;
    public String lasttouched;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
