package me.niloybiswas.spring_lite;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.niloybiswas.spring_lite.annotations.PathVariable;
import me.niloybiswas.spring_lite.annotations.RequestBody;
import me.niloybiswas.spring_lite.annotations.RequestParam;
import me.niloybiswas.spring_lite.core.PathExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.*;

public class DispatcherServlet extends HttpServlet {

    private final List<ControllerMethod> controllerMethodList;
    private final ObjectMapper objectMapper;

    public DispatcherServlet(List<ControllerMethod> controllerMethodList) {
        this.controllerMethodList = controllerMethodList;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        dispatch(req, resp, MethodType.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        dispatch(req, resp, MethodType.POST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        dispatch(req, resp, MethodType.PUT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        dispatch(req, resp, MethodType.DELETE);
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp, MethodType methodType) {
        try {
            String requestURI = req.getRequestURI();
            System.out.println("requestURI = " + requestURI);

            for (ControllerMethod controllerMethod : controllerMethodList) {
                if (controllerMethod.getMethodType() != methodType) {
                    continue;
                }
                String mappedURI = controllerMethod.getUrl();
                if (!PathExtractor.isUrlPatternMatched(mappedURI, requestURI)) {
                    continue;
                }
                // At this stage we have the matching urls
                Map<String, String> pathVariableMap = PathExtractor.getPathVariables(mappedURI, requestURI);
                String requestBody = readRequestBody(req, methodType);
                Object responseObject = invokeMethod(req, resp, controllerMethod, pathVariableMap, requestBody);
                // Write to response
                if (responseObject == null) return;
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(objectMapper.writeValueAsString(responseObject));
                return;
            }
            sendNotFoundResponse(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Object invokeMethod(
            HttpServletRequest req,
            HttpServletResponse resp,
            ControllerMethod controllerMethod,
            Map<String, String> pathVariableMap,
            String requestBody
    ) throws IOException, InvocationTargetException, IllegalAccessException {
        Parameter[] parameters = controllerMethod.getMethod().getParameters();
        Object[] parameterObjects = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                parameterObjects[i] = objectMapper.readValue(requestBody, parameters[i].getType());
            }
            if (parameters[i].isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
                parameterObjects[i] = pathVariableMap.get(pathVariable.value());
            }
            if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                parameterObjects[i] = req.getParameter(requestParam.value());
            }
            if (parameters[i].getType().equals(HttpServletRequest.class)) {
                parameterObjects[i] = req;
            }
            if (parameters[i].getType().equals(HttpServletResponse.class)) {
                parameterObjects[i] = resp;
            }
        }

        return controllerMethod.getMethod().invoke(controllerMethod.getInstance(), parameterObjects);
    }

    private String readRequestBody(HttpServletRequest req, MethodType methodType) {
        if (methodType == MethodType.POST || methodType == MethodType.PUT || methodType == MethodType.DELETE) {
            try {
                BufferedReader reader = req.getReader();
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                return jsonBuilder.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Unable to parse request body");
            }
        }
        return null;
    }

    private void sendNotFoundResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", HttpServletResponse.SC_NOT_FOUND);
        responseMap.put("message", "Requested URL Not Found");
        responseMap.put("url", req.getRequestURL().toString());
        responseMap.put("timestamp", LocalDateTime.now().toString());

        resp.getWriter().write(objectMapper.writeValueAsString(responseMap));
    }
}
