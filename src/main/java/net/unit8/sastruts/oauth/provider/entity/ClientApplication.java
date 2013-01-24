package net.unit8.sastruts.oauth.provider.entity;

import net.unit8.sastruts.oauth.provider.OauthServer;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: UU034251
 * Date: 13/01/17
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public abstract class ClientApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String url;
    public String supportUrl;
    public String callbackUrl;
    public String key;
    public String secret;
    public Long userId;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;

    public String tokenCallbackUrl;

    @Transient
    private OauthServer oauthServer;

    public OauthServer getOauthServer() {
        if (oauthServer == null) {
            oauthServer = new OauthServer("http://your.site");
        }
        return oauthServer;
    }


}
