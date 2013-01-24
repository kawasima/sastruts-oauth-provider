package net.unit8.sastruts.oauth.provider.filter;

import net.unit8.sastruts.oauth.provider.entity.OauthToken;
import net.unit8.sastruts.oauth.provider.service.OauthTokenService;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OauthFilter implements Filter {
    private static final Pattern OAUTH_HEADER_PTN = Pattern.compile("^(Bearer|OAuth|Token) (token=)?([^\\s]*)$");
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		OauthTokenService oauthTokenService = SingletonS2Container.getComponent("oauthTokenService");
		String tokenString = oauth2Token(req);
		if (StringUtil.isNotEmpty(tokenString)) {
			OauthToken token = oauthTokenService.findValidToken(tokenString);
			if (token != null) {
				req.setAttribute("oauth.token", token);
				req.setAttribute("oauth.version", 2);
			}
		}
		filterChain.doFilter(req, res);
	}

	protected String oauth2Token(ServletRequest request) {
		String token = request.getParameter("bearer_token");
		if (StringUtil.isEmpty(token)) {
			token = request.getParameter("access_token");
		}
		if (StringUtil.isEmpty(token)
				&& StringUtil.isNotEmpty(request.getParameter("oauth_token"))
				&& StringUtil.isEmpty(request.getParameter("oauth_signature"))) {
			token = request.getParameter("oauth_token");
		}
        if (StringUtil.isEmpty(token)) {
            HttpServletRequest httpRequest = HttpServletRequest.class.cast(request);
            String auth = httpRequest.getHeader("Authorization");
            if (StringUtil.isNotEmpty(auth) && !StringUtil.contains(auth, "oauth_version=\"1.0\"")) {
                Matcher matcher = OAUTH_HEADER_PTN.matcher(auth);
                if (matcher.find()) {
                    token = matcher.group(3);
                }
            }

        }
		return token;
	}

	public void destroy() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
