package by.bsu.fpmi.auth.service;

import by.bsu.fpmi.auth.dto.UserResp;

public interface JwtService {

    String getPublicKey();

    String generateJwt(UserResp userResp);

    boolean verifyJwt(String jwt);
}
