package net.unit8.sastruts.oauth.provider.logic;

import net.unit8.sastruts.oauth.provider.entity.ClientApplication;
import net.unit8.sastruts.oauth.provider.entity.OauthToken;
import net.unit8.sastruts.oauth.provider.entity.ResourceOwner;
import net.unit8.sastruts.oauth.provider.service.ClientApplicationService;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.BooleanConversionUtil;
import org.seasar.framework.util.StringConversionUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.action.BeanWrapper;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This is conversion
 */
public class ProviderLogic {
    private static final List<String> SUPPORTED_GRANT_TYPE = Arrays.asList(new String[]{
            "authorization_code", "password", "client_credentials"
    });

    @Resource
    protected ClientApplicationService clientApplicationService;

    @Resource
    protected AuthorizerLogic authorizerLogic;

    public String token(BeanMap params) {
        ClientApplication clientApplication = clientApplicationService
                .findByKey(StringConversionUtil.toString(params.get("clientId")));
        if (!StringUtil.equals(clientApplication.secret, StringConversionUtil.toString(params.get("clientSecret")))) {
            oauth2Error("invalid_client");
            return null;
        }

        String grantType = StringConversionUtil.toString(params.get("grantType"));
        if (StringUtil.equals(grantType, "none")) {
            grantType = "client_credentials";
        }
        if (SUPPORTED_GRANT_TYPE.contains(grantType)) {
            GrantLogic grantLogic = SingletonS2Container.getComponent(
                    StringUtil.decapitalize(StringUtil.camelize(grantType)) + "GrantLogic");
            OauthToken token = grantLogic.grant(clientApplication, getParameterBeanMap());
            ResponseUtil.write(token.toJSON(), "application/json");
        } else {
            oauth2Error("unsupported_grant_type");
        }
        return null;
    }

    public String authorize(ResourceOwner resourceOwner, BeanMap params) {
        HttpServletRequest request = RequestUtil.getRequest();

        if (StringUtil.equals(request.getMethod(), "POST")) {
            authorizerLogic.build(
                    resourceOwner,
                    BooleanConversionUtil.toBoolean(params.get("isAuthorized")),
                    getParameterBeanMap());
            try {
                ResponseUtil.getResponse().sendRedirect(authorizerLogic.getRedirectUri().toString());
                return null;
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } else {
            ClientApplication clientApplication = clientApplicationService
                    .findByKey(StringConversionUtil.toString(params.get("clientId")));
            request.setAttribute("clientApplication", new BeanWrapper(clientApplication));
            return "oauth2_authorize.jsp";
        }
    }

    protected void oauth2Error() {
        oauth2Error("invalid_grant");
    }
    protected void oauth2Error(String msg) {
        HttpServletResponse response = ResponseUtil.getResponse();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ResponseUtil.write("{\"error\":\"" + msg + "\"}", "application/json");
    }

    private BeanMap getParameterBeanMap() {
        HttpServletRequest request = RequestUtil.getRequest();
        BeanMap params = new BeanMap();
        Map<String, String[]> reqParams = request.getParameterMap();
        if (reqParams != null) {
            for(Map.Entry<String, String[]> entry : reqParams.entrySet()) {
                String[] values = entry.getValue();
                if (values.length == 1) {
                    if (StringUtil.isEmpty(values[0]))
                        continue;
                    params.put(entry.getKey(), values[0]);
                } else {
                    params.put(entry.getKey(), values);
                }
            }
        }
        return params;
    }


}
