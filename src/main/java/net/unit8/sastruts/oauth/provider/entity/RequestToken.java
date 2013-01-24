package net.unit8.sastruts.oauth.provider.entity;

/**
 * Created with IntelliJ IDEA.
 * User: uu034251
 * Date: 13/01/18
 * Time: 12:38
 * To change this template use File | Settings | File Templates.
 */
public interface RequestToken extends OauthToken {
    public abstract String toQuery();
}
