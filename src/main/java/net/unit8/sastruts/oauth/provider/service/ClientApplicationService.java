package net.unit8.sastruts.oauth.provider.service;


import net.unit8.sastruts.oauth.provider.entity.ClientApplication;
import net.unit8.sastruts.oauth.provider.entity.RequestToken;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: uu034251
 * Date: 13/01/18
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */
public interface ClientApplicationService {
    public abstract ClientApplication find(Long id);
    public abstract ClientApplication findByKey(String key);
    public abstract List<ClientApplication> findAll();
    public abstract RequestToken createRequestToken(ClientApplication clientApplication);

    public abstract int insert(ClientApplication clientApplication);
    public abstract int delete(ClientApplication clientApplication);
    public abstract int update(ClientApplication clientApplication);

    public abstract ClientApplication create(Object bean);
}
