package persistence;

import org.json.JSONObject;

// Represents Writable
// code influenced by JsonSerializationDemo https://github.com/stleary/JSON-java
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
