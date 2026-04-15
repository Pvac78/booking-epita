package dev._xdbe.booking.creelhouse.infrastructure.persistence;


import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

import dev._xdbe.booking.creelhouse.infrastructure.persistence.CryptographyHelper;


@Converter
public class CreditCardConverter implements AttributeConverter<String, String> {

    @Autowired
    private CryptographyHelper cryptographyHelper;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return CryptographyHelper.encryptData(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting credit card", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        String pan;
        try {
            pan = CryptographyHelper.decryptData(dbData);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting credit card", e);
        }
        
        String maskedPanString = panMasking(pan);
        return maskedPanString;
    }

    private String panMasking(String pan) {
        // Step 6
        if (pan == null || pan.length() < 12) {
            return pan;
        }
        return pan.substring(0, 4) + "********" + pan.substring(pan.length() - 4);
        // Step 6: End
    }

    
}