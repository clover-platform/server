package plus.xyc.maven.plugin.i18n.client.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;
    private Boolean success;
    private String message;
    private T data;

}
