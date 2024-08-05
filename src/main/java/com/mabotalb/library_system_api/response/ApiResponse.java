package com.mabotalb.library_system_api.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
}
