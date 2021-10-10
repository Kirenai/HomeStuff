package com.revilla.homestuff.utils.dto.response;

import com.revilla.homestuff.dto.response.ApiResponseDto;
import com.revilla.homestuff.util.GeneralUtil;

public class ApiResponseDataTestUtils {

    public static ApiResponseDto getMockRoleResponse(String messageAction, Class<?> clazz) {
        String message = GeneralUtil.simpleNameClass(clazz) + " " + messageAction;
        return ApiResponseDto.builder().message(message).success(true).build();
    }

}
