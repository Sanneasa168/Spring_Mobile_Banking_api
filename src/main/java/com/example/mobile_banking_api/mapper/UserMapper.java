package com.example.mobile_banking_api.mapper;

import com.example.mobile_banking_api.domain.User;
import com.example.mobile_banking_api.features.auth.dto.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User formRegisterRequest(RegisterRequest registerRequest);

}
