package com.example.api.util;

import javax.ws.rs.core.Response;
import java.util.Collections;

public class ResponseHandler {

    public static Response generate(
            int code, boolean success, String msg, Object data) {

        return Response.status(code)
                .entity(new ApiResponseBody(
                        success, msg,
                        data == null ? Collections.emptyMap() : data))
                .build();
    }

    public static Response ok(String m, Object d) {
        return generate(200, true, m, d);
    }

    public static Response created(String m, Object d) {
        return generate(201, true, m, d);
    }

    public static Response badRequest(String m) {
        return generate(400, false, m, null);
    }

    public static Response unauthorized(String m) {
        return generate(401, false, m, null);
    }

    public static Response notFound(String m) {
        return generate(404, false, m, null);
    }

    public static Response serverError(String m) {
        return generate(500, false, m, null);
    }

    public static class ApiResponseBody {
        public boolean success;
        public String message;
        public Object data;

        public ApiResponseBody(boolean s, String m, Object d) {
            success = s; message = m; data = d;
        }
    }
}

