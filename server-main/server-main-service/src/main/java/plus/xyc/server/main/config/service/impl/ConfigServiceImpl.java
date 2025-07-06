package plus.xyc.server.main.config.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zkit.support.server.account.api.service.AccountConfigApiService;

import plus.xyc.server.main.config.entity.response.CommonConfigResponse;
import plus.xyc.server.main.config.service.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {

    @DubboReference
    private AccountConfigApiService accountConfigApiService;

    @Override
    @Cacheable(value = "config#10min", key = "'common'")
    public CommonConfigResponse common() {
        CommonConfigResponse response = new CommonConfigResponse();
        response.setPublicKey(accountConfigApiService.getTransportPublicKey());
        return response;
    }
}
