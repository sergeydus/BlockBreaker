
package com.example.blockbreaker;

import java.util.HashMap;
import java.util.Map;

public class Me {

    public String username;
    public String nickname;
    public Details_ details;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
