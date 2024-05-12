package plus.xyc.server.main.account.entity.enums;

public enum AccountCode {

    REGISTER_CODE(20001001, "account.register.code.fail"),
    REGISTER_HAS(20001002, "account.register.has"),
    LOGIN_NOT_EXIST(20001003, "account.login.not.exist");

    public final int code;
    public final String key;

    AccountCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
