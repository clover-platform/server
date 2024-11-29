package plus.xyc.server.i18n.branch.entity.enums;

public enum CommitAction {
    ADD(1),
    DELETE(2),
    UPDATE(3);

    private final int value;

    CommitAction(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
