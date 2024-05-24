package plus.xyc.server.i18n.enums;

public enum I18nCode {

    ACCESS_ERROR(999, "i18n.access.error"),

    MODULE_IDENTIFIER_EXIST(1001, "module.identifier.exist"),

    ENTRY_CREATE_BRANCHES(4001, "entry.create.branches"),
    ENTRY_CREATE_KEY(4002, "entry.create.key"),

    ENTRY_COMMENT_FAST(2001, "entry.comment.fast"),

    BRANCH_EXISTED(3001, "branch.existed");

    public final int code;
    public final String key;

    I18nCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
