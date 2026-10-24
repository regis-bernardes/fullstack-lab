package com.dbulinkedin.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class CriptografiaService {

    private static final String ALGORITHM = "AES";

    @Value("${app.security.encryption.secret-key}")
    private String secretKey;

    public String criptografar(String valorOriginal) {
        if (valorOriginal == null) {
            return null;
        }
        
        try {
        	Base64.getDecoder().decode(valorOriginal);
        	return valorOriginal;
        }
        
        catch (Exception ex) {
	        try {
	            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
	            Cipher cipher = Cipher.getInstance(ALGORITHM);
	            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
	            
	            byte[] bytesCifrados = cipher.doFinal(valorOriginal.getBytes(StandardCharsets.UTF_8));
	            return Base64.getEncoder().encodeToString(bytesCifrados);
	        } catch (Exception e) {
	            throw new RuntimeException("Erro ao criptografar o valor", e);
	        }
        }
    }

    public String descriptografar(String valorCriptografado) {
        if (valorCriptografado == null) {
            return null;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] bytesDecodificados = Base64.getDecoder().decode(valorCriptografado);
            byte[] bytesOriginal = cipher.doFinal(bytesDecodificados);
            return new String(bytesOriginal, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar o valor", e);
        }
    }
}