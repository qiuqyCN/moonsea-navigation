package cc.moonsea.navigation.interceptor;

import cc.moonsea.navigation.service.VisitRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class VisitRecordInterceptor implements HandlerInterceptor {

    private final VisitRecordService visitRecordService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // 只记录页面访问，排除静态资源和API调用
        if (!uri.startsWith("/api/") &&
            !uri.startsWith("/admin/api/") &&
            !uri.startsWith("/css/") &&
            !uri.startsWith("/js/") &&
            !uri.startsWith("/images/") &&
            !uri.startsWith("/icons/") &&
            !uri.endsWith(".css") &&
            !uri.endsWith(".js") &&
            !uri.endsWith(".svg") &&
            !uri.endsWith(".png") &&
            !uri.endsWith(".jpg") &&
            !uri.endsWith(".jpeg") &&
            !uri.endsWith(".gif") &&
            !uri.endsWith(".ico")) {

            String ip = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");

            visitRecordService.saveVisitRecord(ip, userAgent, uri);
        }

        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            int index = xForwardedFor.indexOf(",");
            if (index != -1) {
                return xForwardedFor.substring(0, index);
            } else {
                return xForwardedFor;
            }
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
