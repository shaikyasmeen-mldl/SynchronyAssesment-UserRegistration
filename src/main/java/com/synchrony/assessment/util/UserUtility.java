package com.synchrony.assessment.util;

import com.synchrony.assessment.exception.WebException;
import com.synchrony.assessment.model.StatusCode;
import com.synchrony.assessment.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class UserUtility {

    public String decryptPassword(User user) {
        String decryptPassword = "";
        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            String password = user.getPassword();
            alg.reset();
            alg.update(password.getBytes());
            byte[] msgDigest = alg.digest();
            BigInteger number = new BigInteger(1, msgDigest);
            decryptPassword = number.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return decryptPassword;
    }

    public static File convert(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }

    public static String toBase64(File file) {
        try {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(b);
            fs.close();
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        } catch (IOException e) {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    public static HttpURLConnection getHttpConnection(String ClientId, String url, String requestMethod) {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("Authorization", "Client-ID " + ClientId);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        } catch (UnknownHostException e) {
            throw new WebException(StatusCode.UNKNOWN_HOST, e);
        } catch (IOException e) {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    public static void writeToConnection(HttpURLConnection conn, String message) {
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    public static String getResponse(HttpURLConnection conn) {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try {
            if (conn.getResponseCode() != StatusCode.SUCCESS.getHttpCode()) {
                throw new WebException(conn.getResponseCode());
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
        if (str.toString().equals("")) {
            throw new WebException(StatusCode.UNKNOWN_ERROR);
        }
        return str.toString();
    }
}
