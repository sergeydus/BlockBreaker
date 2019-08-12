
package com.example.blockbreaker;

import java.util.HashMap;
import java.util.Map;

public class Details_ {

    public String email;
    public Integer losses;
    public String nickname;
    public String password;
    public Integer wins;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
