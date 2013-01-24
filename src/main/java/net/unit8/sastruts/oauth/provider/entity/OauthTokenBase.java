package net.unit8.sastruts.oauth.provider.entity;

import net.unit8.sastruts.oauth.provider.util.RandomUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: UU034251
 * Date: 13/01/23
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public class OauthTokenBase implements OauthToken {
    public  OauthTokenBase() {
        generateKeys();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long userId;
    public String type;
    public Long clientApplicationId;
    public String token;
    public String secret;
    public String callbackUrl;
    public String verifier; // for oauth1
    public String scope;

    @Temporal(TemporalType.TIMESTAMP)
    public Date authorizedAt;

    @Temporal(TemporalType.TIMESTAMP)
    public Date invalidatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    public Date expiresAt;

    @Transient
    public ResourceOwner user;

    public String toJSON() {
        return "";
    }

    protected void generateKeys() {
        token  = RandomUtil.generateKeys(40).substring(0, 40);
        secret = RandomUtil.generateKeys(40).substring(0, 40);
    }

}
