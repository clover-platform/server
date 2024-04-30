package org.zkit.tg.bot.demo.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zkit.tg.bot.demo.user.entity.dto.User;
import org.zkit.tg.bot.demo.user.entity.response.UserResponse;
import org.zkit.tg.bot.demo.user.mapper.UserMapper;
import org.zkit.tg.bot.demo.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2024-04-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<UserResponse> findUserByUsername(String username) {
        List<User> users = getBaseMapper().findByUsername(username);
        return users.stream().map(user -> {
            UserResponse userResponse = new UserResponse();
            userResponse.setUsername(user.getUsername());
            userResponse.setPassword(user.getPassword());
            return userResponse;
        }).collect(Collectors.toList());
    }
}
