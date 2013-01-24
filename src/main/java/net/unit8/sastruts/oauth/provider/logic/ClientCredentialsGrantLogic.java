package net.unit8.sastruts.oauth.provider.logic;

import net.unit8.sastruts.oauth.provider.entity.ClientApplication;
import net.unit8.sastruts.oauth.provider.entity.OauthToken;
import net.unit8.sastruts.oauth.provider.service.OauthTokenService;
import org.seasar.framework.beans.util.BeanMap;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: UU034251
 * Date: 13/01/17
 * Time: 9:57
 * To change this template use File | Settings | File Templates.
 */
public class ClientCredentialsGrantLogic implements GrantLogic {
    @Resource
    protected OauthTokenService oauthTokenService;

    public OauthToken grant(ClientApplication clientApplication, Map<String, Object> parameter) {
        BeanMap tokenParam = new BeanMap();
        tokenParam.put("clientApplicationId", clientApplication.id);
        tokenParam.put("userId", clientApplication.userId);
        tokenParam.put("scope", parameter.get("scope"));

        // TODO OauthToken token = oauthTokenService.create(tokenParam);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
