package info.caprese.capelline.filter;

import info.caprese.capelline.data.SocialUserData;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Slf4j
public class MDCFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing.
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        setupMdc(servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();

    }

    private void setupMdc(ServletRequest request) {
        try {
            // HTTP要求以外の場合は何もしない
            if (!(request instanceof HttpServletRequest)) {
                return;
            }
            // セッションID
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession ss = httpRequest.getSession(false);
            MDC.put("session_id", ss != null ? ss.getId() : "");

            // userid
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                if (!(authentication.getPrincipal() instanceof SocialUserData)) {
                    SocialUserData principal = (SocialUserData) authentication.getPrincipal();
                    MDC.put("user_id", principal.getUser().getUserId());
                    MDC.put("display_name", principal.getUser().getDisplayName());
                }
            }


        } catch (Exception e) {
            log.warn("unexpected exception: {}", e.getMessage());
        }
    }
    @Override
    public void destroy() {

    }
}
