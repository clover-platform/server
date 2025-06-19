package plus.xyc.server.i18n.activity.entity.enums;

public enum ActivityType {

    MODULE(1, "模块"),
    FILE(2, "文件"),
    BUNDLE(3, "文件包"),
    MEMBER(4, "成员"),
    SETTING(5, "设置"),
    ENTRY(6, "词条");

    public final int code;
    public final String name;

    ActivityType(int code, String name){
        this.code = code;
        this.name = name;
    }

}
