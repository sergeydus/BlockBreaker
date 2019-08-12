
package com.example.blockbreaker;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage {

    public String nickname;
    public String message;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
