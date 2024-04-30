package org.zkit.tg.bot.demo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zkit.tg.bot.demo.user.entity.dto.User;
import org.zkit.tg.bot.demo.user.entity.response.UserResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2024-04-29
 */
public interface UserService extends IService<User> {

    public List<UserResponse> findUserByUsername(String username);

}
