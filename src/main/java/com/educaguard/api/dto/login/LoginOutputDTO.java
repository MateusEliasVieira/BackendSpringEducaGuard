package com.educaguard.api.dto.login;

import com.educaguard.domain.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginOutputDTO {

    @NotNull
    private Long idUser;
    @NotBlank
    private String token;
    @NotNull
    private Roles role;

}
