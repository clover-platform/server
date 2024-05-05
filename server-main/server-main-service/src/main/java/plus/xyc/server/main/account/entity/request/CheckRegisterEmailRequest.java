package plus.xyc.server.main.account.entity.request;

import lombok.Data;

@Data
public class CheckRegisterEmailRequest {

    private String username;
    private String email;
    private String code;

}
