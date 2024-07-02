package com.example.mobile_banking_api.features.fileupload;

import lombok.Builder;

@Builder
public record FileUploadResponse(
        String name,

        String uri,

        String contentType,

        Long size
) {
}
