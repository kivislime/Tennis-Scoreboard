package org.kivislime.tennisscoreboard.util;

import jakarta.servlet.http.HttpServletResponse;
import org.kivislime.tennisscoreboard.ErrorResponse;

import java.io.IOException;

public class ServletUtil {
    public static void sendJsonError(HttpServletResponse response, int statusCode, ErrorResponse errorResponse) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(errorResponse));
    }
}
