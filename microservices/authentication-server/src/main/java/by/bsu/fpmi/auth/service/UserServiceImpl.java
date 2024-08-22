package by.bsu.fpmi.auth.service;

import by.bsu.fpmi.auth.dto.UserReq;
import by.bsu.fpmi.auth.dto.UserResp;
import by.bsu.fpmi.auth.entity.User;
import by.bsu.fpmi.auth.hashing.Hasher;
import by.bsu.fpmi.auth.mapper.UserMapper;
import by.bsu.fpmi.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Hasher hasher;
    private final JwtService jwtService;

    @Override
    public Optional<UserResp> saveUser(UserReq userReq) {
        User userToSave = userMapper.fromReq(userReq);
        try {
            User savedUser = userRepository.saveAndFlush(userToSave);
            return Optional.of(userMapper.toResp(savedUser));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> generateJwtIfUserValid(UserReq userReq) {
        Optional<User> optionalUser = userRepository.findByUserName(userReq.getUserName());
        return optionalUser
                .map(user -> {
                    String encodedPassword = user.getEncodedPassword();
                    String givenPassword = userReq.getRawPassword();
                    if (!hasher.verifyHash(givenPassword, encodedPassword)) {
                        return null;
                    }

                    UserResp userResp = UserResp.builder()
                            .userName(user.getUserName())
                            .role(user.getRole())
                            .build();
                    return jwtService.generateJwt(userResp);
                });
    }


}
