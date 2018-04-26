package br.com.stevanini.espplay.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Romulo on 21/05/2017.
 */

public class Utils {

    public static String stringCryptografy(String senha){
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
            byte messageDigestSenhaAdmin[] = algorithm.digest(senha.getBytes("UTF-8"));
            StringBuilder hexStringSenhaAdmin = new StringBuilder();
            for (byte b : messageDigestSenhaAdmin) {
                hexStringSenhaAdmin.append(String.format("%02X", 0xFF & b));
            }
            return hexStringSenhaAdmin.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
