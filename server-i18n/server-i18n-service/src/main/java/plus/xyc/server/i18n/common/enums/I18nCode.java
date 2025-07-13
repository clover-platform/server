package plus.xyc.server.i18n.common.enums;

public enum I18nCode {

    ACCESS_ERROR(999, "i18n.access.error"),

    MODULE_IDENTIFIER_EXIST(1001, "module.identifier.exists"),
    MODULE_NOT_FOUND(1002, "module.not.found"),
    MODULE_COLLECT_EXIST(1003, "module.collect.exists"),

    ENTRY_CREATE_FILES(2001, "entry.create.files"),
    ENTRY_CREATE_KEY(2002, "entry.create.key"),

    ENTRY_RESULT_EXIST(3001, "entry.result.exists"),

    ENTRY_COMMENT_FAST(4001, "entry.comment.fast"),

    MEMBER_INVITE_EXPIRED(10033, "member.invite.expired"),
    MEMBER_JOINED(10032, "member.invite.joined"),

    USER_NOT_FOUND(6001, "user.not.found");

    public final int code;
    public final String key;

    I18nCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
