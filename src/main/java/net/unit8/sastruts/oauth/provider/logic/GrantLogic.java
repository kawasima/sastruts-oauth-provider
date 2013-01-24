package net.unit8.sastruts.oauth.provider.logic;


import net.unit8.sastruts.oauth.provider.entity.ClientApplication;
import net.unit8.sastruts.oauth.provider.entity.OauthToken;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: UU034251
 * Date: 13/01/17
 * Time: 9:58
 * To change this template use File | Settings | File Templates.
 */
public interface GrantLogic {
    public OauthToken grant(ClientApplication clientApplication, Map<String, Object> parameter);
}
