package org.zkit.tg.bot.demo.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zkit.tg.bot.demo.user.entity.response.UserResponse;
import org.zkit.tg.bot.demo.user.service.UserService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2024-04-29
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@RefreshScope
public class UserController {

    private UserService userService;
    @Value("${redis.host}")
    private String host;

    @GetMapping("/list")
    List<UserResponse> list() {
        return userService.findUserByUsername("test");
    }

    @GetMapping("/test")
    String test() {
        log.info(host);
        return host;
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
