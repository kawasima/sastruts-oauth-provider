package net.unit8.sastruts.oauth.provider.logic;

import net.unit8.sastruts.oauth.provider.entity.*;
import net.unit8.sastruts.oauth.provider.service.ClientApplicationService;
import net.unit8.sastruts.oauth.provider.service.OauthTokenService;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.StringConversionUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.URLEncoderUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: UU034251
 * Date: 12/12/28
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public class AuthorizerLogic {
    @Resource
    protected OauthTokenService oauthTokenService;

    @Resource
    protected ClientApplicationService clientApplicationService;

    private boolean authorized;
    private ResourceOwner resourceOwner;
    private ClientApplication clientApplication;
    private BeanMap params;
    private OauthToken code;
    private OauthToken token;

    public void build(ResourceOwner resourceOwner, Boolean authorized, BeanMap params) {
        this.resourceOwner = resourceOwner;
        this.authorized = authorized;
        this.params = params;
    }
    public boolean isAuthorized() {
        return authorized;
    }

    public ClientApplication getClientApplication() {
        if (clientApplication == null) {
            clientApplication = clientApplicationService.findByKey(
                    StringConversionUtil.toString(params.get("client_id")));
        }
        return clientApplication;
    }

    public URL getRedirectUri() {
        HttpServletRequest request = RequestUtil.getRequest();
        URL url = getBaseUri(request);
        if (StringUtil.equals(request.getParameter("response_type"), "code")) {
            StringBuilder query = new StringBuilder(url.getQuery() == null ? "" : url.getQuery());
            if (query.length() > 0) {
                query.append("&");
            }
            query.append(encodeResponse());
            try {
                return new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), query.toString(), null).toURL();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } else {
            try {
                return new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
                    encodeResponse()).toURL();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
    }

    public OauthToken getCode() {
        if (code == null) {
            BeanMap oauthTokenParams = new BeanMap();
            oauthTokenParams.put("clientApplicationId", getClientApplication().id);
            oauthTokenParams.put("userId", resourceOwner.getResourceOwnerId());
            if (params.containsKey("scope"))
                oauthTokenParams.put("scope", params.get("scope"));
            if (params.containsKey("redirect_uri"))
                oauthTokenParams.put("callbackUrl", params.get("redirect_uri"));
            code = oauthTokenService.create(Oauth2Verifier.class, oauthTokenParams);
        }
        return code;
    }

    public OauthToken getToken() {
        if (token == null) {
            BeanMap oauthTokenParams = new BeanMap();
            oauthTokenParams.put("clientApplicationId", getClientApplication().id);
            oauthTokenParams.put("userId", resourceOwner.getResourceOwnerId());
            if (params.containsKey("scope"))
                oauthTokenParams.put("scope", params.get("scope"));
            if (params.containsKey("redirect_uri"))
                oauthTokenParams.put("callbackUrl", params.get("redirect_uri"));
            token = oauthTokenService.create(Oauth2Token.class, oauthTokenParams);
        }
        return token;
    }

    protected Map<String, Object> response() {
        Map<String, Object> r = CollectionsUtil.newHashMap();
        String responseType = StringConversionUtil.toString(params.get("response_type"));
        if (StringUtil.equals(responseType, "token") || StringUtil.equals(responseType, "code")) {
            if (authorized) {
                if (StringUtil.equals(responseType, "code")) {
                    r.put("code", ((OauthTokenBase)getCode()).token);
                } else {
                    r.put("access_token", ((OauthTokenBase)getCode()).token);
                }
            } else {
                r.put("error", "access_denied");
            }
        } else {
            r.put("error", "unsupported_response_type");
        }
        if (params.containsKey("state"))
            r.put("state", params.get("state"));
        return r;
    }

    protected String encodeResponse() {
        StringBuilder encRes = new StringBuilder();
        for(Map.Entry<String, Object> entry : response().entrySet()) {
            encRes.append(URLEncoderUtil.encode(entry.getKey()))
                    .append("=")
                    .append(URLEncoderUtil.encode(StringConversionUtil.toString(entry.getValue())))
                    .append("&");
        }
        if (encRes.charAt(encRes.length() - 1) == '&')
            encRes.deleteCharAt(encRes.length() - 1);
        return encRes.toString();
    }
    protected URL getBaseUri(HttpServletRequest request) {
        if (StringUtil.isNotEmpty(request.getParameter("redirect_url"))) {
            return URLUtil.create(request.getParameter("redirect_url"));
        } else {
            return URLUtil.create(getClientApplication().callbackUrl);
        }
    }

}
