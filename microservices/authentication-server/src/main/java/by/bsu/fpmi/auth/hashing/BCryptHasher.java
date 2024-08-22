package by.bsu.fpmi.auth.hashing;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptHasher implements Hasher {
    private final static int LOG_ROUNDS = 10;

    @Override
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
    }

    @Override
    public boolean verifyHash(String candidatePassword, String storedHash) {

        if (candidatePassword == null || storedHash == null) {
            throw new IllegalArgumentException("Null values are forbidden");
        }

        return BCrypt.checkpw(candidatePassword, storedHash);
    }
}
