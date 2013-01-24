package net.unit8.sastruts.oauth.provider.logic;

import net.unit8.sastruts.oauth.provider.entity.ClientApplication;
import net.unit8.sastruts.oauth.provider.entity.Oauth2Token;
import net.unit8.sastruts.oauth.provider.entity.Oauth2Verifier;
import net.unit8.sastruts.oauth.provider.entity.OauthToken;
import net.unit8.sastruts.oauth.provider.service.OauthTokenService;
import org.seasar.framework.util.StringConversionUtil;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: UU034251
 * Date: 13/01/17
 * Time: 11:34
 * To change this template use File | Settings | File Templates.
 */
public class AuthorizationCodeGrantLogic implements GrantLogic {
    @Resource
    protected OauthTokenService oauthTokenService;

    public OauthToken grant(ClientApplication clientApplication, Map<String, Object> parameter) {
        Oauth2Verifier verificationCode = oauthTokenService.findByToken(
                Oauth2Verifier.class,
                clientApplication.id,
                StringConversionUtil.toString(parameter.get("code")));
        return oauthTokenService.exchange(verificationCode, Oauth2Token.class);
    }
}
