package plus.xyc.server.main.account.entity.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestCheckRequest {

    @NotNull
    @Min(100)
    private Integer size;
    @Email
    @NotNull
    private String email;

}
