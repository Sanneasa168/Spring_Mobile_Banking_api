package com.example.mobile_banking_api.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse <T>  {
    private Integer code;
    private T reason;
}
