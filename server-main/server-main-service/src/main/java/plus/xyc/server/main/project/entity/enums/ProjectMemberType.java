package plus.xyc.server.main.project.entity.enums;

public enum ProjectMemberType {

    MEMBER(0, "普通成员"),
    ADMIN(1, "管理员"),
    OWNER(2, "创建者");

    public final int code;
    public final String name;

    ProjectMemberType(int code, String name){
        this.code = code;
        this.name = name;
    }

}
