package plus.xyc.server.i18n.activity.entity.enums;

public enum ActivityOperate {

    ADD(1, "添加"),
    UPDATE(2, "更新"),
    DELETE(3, "删除"),
    APPROVE(4, "授权"),
    REMOVE_APPROVAL(5, "撤销授权");

    public final int code;
    public final String name;

    ActivityOperate(int code, String name){
        this.code = code;
        this.name = name;
    }

}
