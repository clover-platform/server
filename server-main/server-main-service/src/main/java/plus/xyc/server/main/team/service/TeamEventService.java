package plus.xyc.server.main.team.service;

public interface TeamEventService {

    void join(Long userId, Long teamId);
    void leave(Long userId, Long teamId);
    void update(Long userId, Long teamId);
    void delete(Long userId, Long teamId);
    void create(Long userId, Long teamId);

}
