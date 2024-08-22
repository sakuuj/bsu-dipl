package integration;

import by.bsu.fpmi.auth.dto.UserReq;
import by.bsu.fpmi.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

@Data
@With
@Builder
@NoArgsConstructor(staticName = "aUser")
@AllArgsConstructor
public class UserTestBuilder {

    private long id = 10L;

    private String userName = "Paul";

    private String rawPassword = "qwertyABOBA";

    private String encodedPassword = "$2a$10$mW6UHeDhs6oKkeF6pw/hLuSFsRxrfb.3st8.RKOfBQM/zdSNKS77C";

    private String role = "ADMIN";


    public User build() {
        return new User(id, userName, encodedPassword, role);
    }

    public UserReq buildReq() {
        return new UserReq(userName, rawPassword);
    }
}
