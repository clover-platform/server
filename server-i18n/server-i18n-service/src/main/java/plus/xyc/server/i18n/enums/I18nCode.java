package plus.xyc.server.i18n.enums;

public enum I18nCode {

    MODULE_IDENTIFIER_EXIST(1001, "module.identifier.exist");

    public final int code;
    public final String key;

    I18nCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
