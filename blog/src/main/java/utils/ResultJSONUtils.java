package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/*
    统一的信息输出类
 */
public class ResultJSONUtils {

    public static void write(HttpServletResponse response,String jsonString) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonString);
    }

    public static void writeMap(HttpServletResponse response, HashMap map) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        printWriter.println(objectMapper.writeValueAsString(map));
    }
}
