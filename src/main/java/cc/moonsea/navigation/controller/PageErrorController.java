package cc.moonsea.navigation.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class PageErrorController implements ErrorController {


    @GetMapping
    public String clientError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorTitle = "";
        String errorMessage = "";

        if (statusCode != null) {
            errorMessage = switch (statusCode) {
                case 400 -> {
                    errorTitle = "错误请求";
                    yield "服务器无法理解您的请求。";
                }
                case 401 -> {
                    errorTitle = "未授权";
                    yield "您需要登录才能访问此页面。";
                }
                case 403 -> {
                    errorTitle = "禁止访问";
                    yield "您没有权限访问此页面。";
                }
                case 404 -> {
                    errorTitle = "页面未找到";
                    yield "您访问的页面不存在或已被移除。";
                }
                case 405 -> {
                    errorTitle = "方法不允许";
                    yield "请求方法不被允许。";
                }
                case 429 -> {
                    errorTitle = "请求过多";
                    yield "您的请求过于频繁，请稍后再试。";
                }
                case 500 -> {
                    errorTitle = "内部服务器错误";
                    yield "服务器遇到了一个错误，请稍后重试。";
                }
                case 502 -> {
                    errorTitle = "网关错误";
                    yield "网关服务器错误。";
                }
                case 503 -> {
                    errorTitle = "服务不可用";
                    yield "服务暂时不可用，请稍后再试。";
                }
                case 504 -> {
                    errorTitle = "网关超时";
                    yield "网关超时。";
                }
                default -> {
                    if (statusCode >= 400 && statusCode < 500) {
                        errorTitle = "客户端错误";
                        yield "您请求的页面出现了客户端错误。";
                    } else {
                        errorTitle = "服务器错误";
                        yield "服务器遇到了一个错误，请稍后重试。";
                    }

                }
            };
        }

        model.addAttribute("status", statusCode != null ? statusCode.toString() : "4xx");
        model.addAttribute("error", errorTitle);
        model.addAttribute("message", errorMessage);

        return null != statusCode
                && statusCode >= 400
                && statusCode < 500 ? "error/4xx" : "error/5xx";
    }
}
