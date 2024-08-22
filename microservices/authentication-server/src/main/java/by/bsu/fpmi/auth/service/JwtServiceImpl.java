package by.bsu.fpmi.auth.service;

import by.bsu.fpmi.auth.dto.UserResp;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtServiceImpl implements JwtService {

    OctetKeyPair jwk = new OctetKeyPairGenerator(Curve.Ed25519).generate();

    OctetKeyPair publicJWK = jwk.toPublicJWK();

    JWSSigner signer = new Ed25519Signer(jwk);
    JWSVerifier verifier = new Ed25519Verifier(publicJWK);

    private final int MINUTES_BEFORE_JWT_EXPIRES = 30;


    public JwtServiceImpl() throws Exception {
    }

    public String getPublicKey() {
        return publicJWK.toJSONString();
    }
    public String generateJwt(UserResp userResp) {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userResp.getUserName())
                .claim("role", userResp.getRole())
                .issuer("cfg-research")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000 * MINUTES_BEFORE_JWT_EXPIRES))
                .build();

        SignedJWT signedJWT = null;
        try {
            signedJWT = jwtFromClaims(claimsSet);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return signedJWT.serialize();
    }

    public boolean verifyJwt(String jwt) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            return signedJWT.verify(verifier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SignedJWT jwtFromClaims(JWTClaimsSet claimsSet) throws JOSEException {
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.EdDSA).build(),
                claimsSet);
        signedJWT.sign(signer);
        return signedJWT;
    }


}
