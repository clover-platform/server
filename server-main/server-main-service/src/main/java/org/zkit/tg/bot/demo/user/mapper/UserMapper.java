package org.zkit.tg.bot.demo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.zkit.tg.bot.demo.user.entity.dto.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author generator
 * @since 2024-04-29
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> findByUsername(@Param("username") String username);

}
