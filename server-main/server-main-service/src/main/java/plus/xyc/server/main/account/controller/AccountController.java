package plus.xyc.server.main.account.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.support.server.account.api.entity.AccountResponse;
import org.zkit.support.server.account.api.rest.AuthAccountApi;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-05-02
 */
@RestController
@RequestMapping("/api/main/account")
@Slf4j
public class AccountController {

    private AuthAccountApi authAccountApi;

    @GetMapping("/test")
    public AccountResponse test(@Param("username") String username) {
        AccountResponse response = authAccountApi.findByUsername(username);
        log.info("AccountController test {}", response);
        return response;
    }

    @Autowired
    public void setAuthAccountApi(AuthAccountApi authAccountApi) {
        this.authAccountApi = authAccountApi;
    }
}
