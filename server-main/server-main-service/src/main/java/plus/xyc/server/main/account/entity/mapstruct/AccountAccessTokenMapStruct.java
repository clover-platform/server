package plus.xyc.server.main.account.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import plus.xyc.server.main.account.entity.dto.AccountAccessToken;
import plus.xyc.server.main.account.entity.request.AccountAccessTokenCreateRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountAccessTokenMapStruct {

    @Mapping(target = "scopes", source = "scopes")
    AccountAccessToken toAccountAccessToken(AccountAccessTokenCreateRequest request);

}
