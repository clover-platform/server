package plus.xyc.server.main.team.entity.enums;

public enum TeamMemberType {

    MEMBER(0, "普通成员"),
    ADMIN(1, "管理员"),
    OWNER(2, "创建者");

    public final int code;
    public final String name;

    TeamMemberType(int code, String name){
        this.code = code;
        this.name = name;
    }

}
