package plus.xyc.server.main.enums;

public enum MainCode {

    ACCESS_DENIED(9000, "access.denied"),

    ACCESS_TOKEN_NAME_EXISTS(10000, "access.token.name.exists"),
    ACCESS_TOKEN_REJECT(10001, "access.token.reject"),

    ACCOUNT_NOT_EXIST(20001000, "account.not.exist"),
    REGISTER_CODE(20001001, "account.register.code.fail"),
    REGISTER_HAS(20001002, "account.register.has"),
    LOGIN_NOT_EXIST(20001003, "account.login.not.exist"),

    RESET_CODE(20001004, "account.reset.code.fail"),

    TEAM_REPEATED(20002000, "team.key.repeated"),
    CURRENT_TEAM_DELETE(20002001, "team.current.delete"),
    TEAM_DELETE_LAST(20002002, "team.last.delete"),

    PROJECT_REPEATED(20003001, "project.key.repeated"),
    CURRENT_PROJECT(20003002, "project.current.delete"),
    PROJECT_LAST(20003003, "project.last.delete"),

    EMAIL_CODE(20004001, "account.email.code.fail");

    public final int code;
    public final String key;

    MainCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
