package plus.xyc.server.i18n.member.entity.enums;

public enum MemberRoleType {

    VIEW(0, "查看"),
    ADMIN(1, "管理员"),
    OWNER(2, "创建者"),
    TRANSLATOR(3, "翻译"),
    CHECK(4, "校验");

    public final int code;
    public final String name;

    MemberRoleType(int code, String name){
        this.code = code;
        this.name = name;
    }

}
