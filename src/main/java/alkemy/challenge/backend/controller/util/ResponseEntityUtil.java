package alkemy.challenge.backend.controller.util;

import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntityUtil {

    public static ResponseEntity<Map<String, Object>> generateResponse(HttpStatus httpStatus, String objectName, Object object){
        Map<String, Object> bodyResponse = new HashMap<>();
        if(object == null){
            return new ResponseEntity<>(httpStatus);
        }
        bodyResponse.put(objectName, object);
        return new ResponseEntity<>(bodyResponse, httpStatus);
    }
}
