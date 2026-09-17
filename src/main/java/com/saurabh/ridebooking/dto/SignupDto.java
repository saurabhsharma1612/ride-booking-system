package com.saurabh.ridebooking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @NotBlank(message = "Name is required")
    private String name;
    private String email;
    private String password;
}
