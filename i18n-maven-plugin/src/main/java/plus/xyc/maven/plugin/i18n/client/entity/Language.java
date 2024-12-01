package plus.xyc.maven.plugin.i18n.client.entity;

import lombok.Data;

@Data
public class Language {

    private String code;
    private String i18n;
    private Boolean source = false;

    public String getI18n() {
        if(i18n == null)
            return code;
        return i18n;
    }
}
