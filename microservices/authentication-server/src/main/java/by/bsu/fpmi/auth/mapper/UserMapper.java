package by.bsu.fpmi.auth.mapper;

import by.bsu.fpmi.auth.dto.UserReq;
import by.bsu.fpmi.auth.dto.UserResp;
import by.bsu.fpmi.auth.entity.User;
import by.bsu.fpmi.auth.hashing.Hasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final Hasher hasher;

    public User fromReq(UserReq userReq) {
        String encodedPassword = hasher.hash(userReq.getRawPassword());

        return new User(-1L, userReq.getUserName(), encodedPassword,"USER");
    }

    public UserResp toResp(User user) {
        return UserResp.builder()
                .userName(user.getUserName())
                .role(user.getRole())
                .build();
    }
}
