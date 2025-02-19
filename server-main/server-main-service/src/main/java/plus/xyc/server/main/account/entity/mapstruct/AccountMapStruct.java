package plus.xyc.server.main.account.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.zkit.support.server.account.api.entity.request.AccountRegisterRequest;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.request.RegisterRequest;
import plus.xyc.server.main.account.entity.response.AccountProfileResponse;
import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapStruct {

    AccountProfileResponse toAccountProfileResponse(Account account);
    ApiAccountResponse toApiAccountResponse(Account account);
    @Mapping(target = "username", source = "email")
    AccountRegisterRequest toAccountRegisterRequestFromRegisterRequest(RegisterRequest request);

}
