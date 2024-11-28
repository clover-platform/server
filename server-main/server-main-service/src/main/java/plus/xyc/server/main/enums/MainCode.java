package plus.xyc.server.main.enums;

public enum MainCode {

    ACCESS_TOKEN_NAME_EXISTS(10000, "access.token.name.exists"),
    ACCESS_TOKEN_REJECT(10001, "access.token.reject");

    public final int code;
    public final String key;

    MainCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
