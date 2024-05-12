package plus.xyc.server.main.account.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.main.account.entity.dto.Account;
import plus.xyc.server.main.account.entity.response.AccountProfileResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapStruct {

    AccountProfileResponse toAccountProfileResponse(Account account);

}
