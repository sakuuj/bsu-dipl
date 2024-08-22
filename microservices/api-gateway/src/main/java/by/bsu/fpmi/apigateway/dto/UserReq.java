package by.bsu.fpmi.apigateway.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReq {

    @Pattern(regexp = "[a-zA-Z0-9]{4,50}", message = "Формат имени: [a-zA-Z0-9]{4,50}")
    @NotNull
    private String userName;

    @Pattern(regexp = "[a-zA-Z0-9_]{4,100}",message = "Формат пароля: [a-zA-Z0-9_]{4,100}" )
    @NotNull
    private String rawPassword;
}
