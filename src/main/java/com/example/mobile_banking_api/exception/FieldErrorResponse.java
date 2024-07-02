package com.example.mobile_banking_api.exception;

import lombok.Builder;

@Builder
public record FieldErrorResponse(
        String field,
        String detail
) {
}
