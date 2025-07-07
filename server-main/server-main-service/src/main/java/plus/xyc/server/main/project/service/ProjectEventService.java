package plus.xyc.server.main.project.service;

public interface ProjectEventService {

    void join(Long userId, Long projectId, Long teamId);
    void leave(Long userId, Long projectId, Long teamId);
    void update(Long userId, Long projectId, Long teamId);
    void delete(Long userId, Long projectId, Long teamId);
    void create(Long userId, Long projectId, Long teamId);

}
