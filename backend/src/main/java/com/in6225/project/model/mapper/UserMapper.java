package com.in6225.project.model.mapper;

import com.in6225.project.model.dto.UserBasicDTO;
import com.in6225.project.model.dto.UserDetailsDTO;
import com.in6225.project.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    void toUser(@MappingTarget User user, UserBasicDTO dto);
    void toUser(@MappingTarget User user, UserDetailsDTO dto);

    UserBasicDTO toUserBasicDTO(User user);
    UserDetailsDTO toUserDetailsDTO(User user);
}
