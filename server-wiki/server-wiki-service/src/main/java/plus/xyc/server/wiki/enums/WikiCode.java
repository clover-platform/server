package plus.xyc.server.wiki.enums;

public enum WikiCode {

    BOOK_PATH_EXISTED(10001, "book.path.existed");

    public final int code;
    public final String key;

    WikiCode(int code, String key){
        this.code = code;
        this.key = key;
    }

}
