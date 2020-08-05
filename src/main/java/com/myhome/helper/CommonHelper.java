package com.myhome.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonHelper {
    public static String getStringFromObject(Object object) {
        getObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonString = "";
        try {
            jsonString = getObjectMapper().writeValueAsString(object).replaceAll("\n", "");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonString;
    }

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
    public static boolean empty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).isEmpty();
        }
        if (obj instanceof List) {
            return ((List) obj).isEmpty();
        }
        if (obj instanceof Set) {
            return ((Set) obj).isEmpty();
        }
        if (obj instanceof String[]) {
            return ((String[]) obj).length == 0;
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

}
