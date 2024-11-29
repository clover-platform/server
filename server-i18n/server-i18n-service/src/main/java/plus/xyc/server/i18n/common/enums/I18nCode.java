package plus.xyc.server.i18n.common.enums;

public enum I18nCode {

    ACCESS_ERROR(999, "i18n.access.error"),

    MODULE_IDENTIFIER_EXIST(1001, "module.identifier.exists"),
    MODULE_NOT_FOUND(1002, "module.not.found"),

    ENTRY_CREATE_BRANCHES(2001, "entry.create.branches"),
    ENTRY_CREATE_KEY(2002, "entry.create.key"),

    ENTRY_RESULT_EXIST(3001, "entry.result.exists"),

    ENTRY_COMMENT_FAST(4001, "entry.comment.fast"),

    BRANCH_EXISTED(5001, "branch.existed"),

    MEMBER_INVITE_EXPIRED(10033, "member.invite.expired"),
    MEMBER_JOINED(10032, "member.invite.joined"),

    BUNDLE_EXISTED(6001, "bundle.existed");

    public final int code;
    public final String key;

    I18nCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
