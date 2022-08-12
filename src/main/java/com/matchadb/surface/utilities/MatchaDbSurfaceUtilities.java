package com.matchadb.surface.utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MatchaDbSurfaceUtilities {

    /**
     * This method converts an object to a JSON String
     *
     * @param object The object to convert to a JSON String
     *
     * @return the converted JSON String
     */
    public static String objectToJSONString(Object object) {
        String jsonString = "";

        if (object instanceof List objectAsList) {
            jsonString += "[";
            for (Object item : objectAsList) {
                jsonString += objectToJSONString(item);
            }
            jsonString += "]";
        } else if (object instanceof HashMap objectAsHashMap) {
            jsonString += "{";
            for (Iterator mapIterator = objectAsHashMap.keySet().iterator(); mapIterator.hasNext();) {
                String key = (String) mapIterator.next();
                Object value = objectAsHashMap.get(key);
                jsonString += "\"" + key + "\":" + objectToJSONString(value);
                if (mapIterator.hasNext()) {
                    jsonString += ",";
                }
            }
            jsonString += "}";
        } else if (object instanceof Boolean objectAsBoolean) {
            jsonString = objectAsBoolean.toString();
        } else if (object instanceof Long objectAsLong) {
            jsonString = objectAsLong.toString();
        } else if (object instanceof Double objectAsDouble) {
            jsonString = objectAsDouble.toString();
        } else {
            jsonString = "\"" + object.toString() + "\"";
        }

        return jsonString;
    }

}
