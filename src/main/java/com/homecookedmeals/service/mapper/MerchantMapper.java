package com.homecookedmeals.service.mapper;

import com.homecookedmeals.domain.Merchant;
import com.homecookedmeals.service.dto.MerchantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Merchant} and its DTO {@link MerchantDTO}.
 */
@Mapper(componentModel = "spring")
public interface MerchantMapper extends EntityMapper<MerchantDTO, Merchant> {}
