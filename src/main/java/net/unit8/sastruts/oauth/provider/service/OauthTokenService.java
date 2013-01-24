package net.unit8.sastruts.oauth.provider.service;

import net.unit8.sastruts.oauth.provider.entity.OauthToken;
import org.seasar.framework.beans.util.BeanMap;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: UU034251
 * Date: 13/01/08
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public interface OauthTokenService {
    public abstract boolean isAuthorized(OauthToken token);

    public abstract OauthToken findValidToken(String token);

    public abstract List<OauthToken> findAllByResourceOwner(Long resourceOwnerId);

    public abstract <TOKEN extends OauthToken> TOKEN create(Class<TOKEN> tokenClass, BeanMap params);
    public abstract <TOKEN extends OauthToken> TOKEN findByToken(Class<TOKEN> tokenClass, Long clientApplicationId, String token);
    public abstract <FT extends OauthToken, TT extends OauthToken> TT exchange(FT fromToken, Class<TT> toTokenClass);

    public abstract int insert(OauthToken oauthToken);
}
