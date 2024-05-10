package me.silvana23.todo.core.security;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.HexFormat;

@Singleton
@RequiredArgsConstructor
public class PasswordUtil {
    private final Logger logger;

    @ConfigProperty(name = "password.key")
    String passwordKey;

    @SneakyThrows
    public String encode(String plaintext) {
        KeySpec spec = new PBEKeySpec(plaintext.toCharArray(), passwordKey.getBytes(StandardCharsets.UTF_8), 65536 * 4, 512);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        return HexFormat.of().formatHex(hash);
    }

    public boolean verify(String encoded, String plaintext) {
        String plaintextHashed = encode(plaintext);

        logger.debug("Validating password.\n\tHashed Plaintext: %s\n\tEncoded: %s\n\tMatches: %s".formatted(
                plaintextHashed,
                encoded,
                encoded.equals(plaintextHashed)
        ));

        return encoded.equals(plaintextHashed);
    }
}
