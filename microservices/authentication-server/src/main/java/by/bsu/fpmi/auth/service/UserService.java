package by.bsu.fpmi.auth.service;

import by.bsu.fpmi.auth.dto.UserReq;
import by.bsu.fpmi.auth.dto.UserResp;

import java.util.Optional;

public interface UserService {

    Optional<UserResp> saveUser(UserReq userReq);

    Optional<String> generateJwtIfUserValid(UserReq userReq);
}
