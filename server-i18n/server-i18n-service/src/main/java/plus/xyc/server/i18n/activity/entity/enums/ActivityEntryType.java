package plus.xyc.server.i18n.activity.entity.enums;

public enum ActivityEntryType {

    ENTRY(1, "词条"),
    TRANSLATE(2, "翻译"),
    COMMENT(3, "评论");

    public final int code;
    public final String name;

    ActivityEntryType(int code, String name){
        this.code = code;
        this.name = name;
    }

}
