package com.homecookedmeals.service.mapper;

import com.homecookedmeals.domain.Merchant;
import com.homecookedmeals.domain.User;
import com.homecookedmeals.service.dto.MerchantDTO;
import com.homecookedmeals.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Merchant} and its DTO {@link MerchantDTO}.
 */
@Mapper(componentModel = "spring")
public interface MerchantMapper extends EntityMapper<MerchantDTO, Merchant> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    MerchantDTO toDto(Merchant s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
