package by.bsu.fpmi.apigateway.service;

import by.bsu.fpmi.apigateway.dto.UserResp;
import by.bsu.fpmi.apigateway.httpclient.AuthenticationServerClient;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl {

    private final AuthenticationServerClient httpClient;

    private volatile OctetKeyPair publicJWK;
    private volatile JWSVerifier jwsVerifier;

    private final Lock lock = new ReentrantLock();


    public Optional<SignedJWT> verifyJwt(String jwt) {
        try {
            if (publicJWK == null) {
                try {
                    lock.lock();
                    if (publicJWK == null) {
                        String publicKey = httpClient.getPublicKey().getBody();
                        if (publicKey == null) {
                            throw new RuntimeException("public key is null");
                        }
                        publicJWK = OctetKeyPair.parse(publicKey);
                        jwsVerifier = new Ed25519Verifier(publicJWK);
                    }
                } finally {
                    lock.unlock();
                }
            }

            SignedJWT signedJWT = SignedJWT.parse(jwt);
            if (!signedJWT.verify(jwsVerifier)) {
                return Optional.empty();
            }
            return Optional.of(signedJWT);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserResp userRespFromJwt(SignedJWT signedJWT) {
        try {
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            return UserResp.builder()
                    .userName(jwtClaimsSet.getSubject())
                    .role((String) jwtClaimsSet.getClaim("role"))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
