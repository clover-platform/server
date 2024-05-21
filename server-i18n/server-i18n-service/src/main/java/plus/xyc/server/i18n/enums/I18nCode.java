package plus.xyc.server.i18n.enums;

public enum I18nCode {

    MODULE_IDENTIFIER_EXIST(1001, "module.identifier.exist"),

    ENTRY_COMMENT_FAST(2001, "entry.comment.fast");

    public final int code;
    public final String key;

    I18nCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
