package org.zkit.tg.bot.demo.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    private UserService userService;

    @GetMapping("/list")
    List<UserResponse> list() {
        return userService.findUserByUsername("test");
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
