
package com.example.blockbreaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Players {

    public List<User> users = null;
    public Me me;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
