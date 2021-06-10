package mamn13.sem2021b.course22923.webapi;


import mamn13.sem2021b.course22923.logic.Crypto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class CryptoController {

    @PostMapping(path = "/encrypt")
    public String encrypt(@RequestParam String key, @RequestParam String string) {
        try {
            return Crypto.encryptString(key, string);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @PostMapping(path = "/decrypt")
    public String decrypt(@RequestParam String key, @RequestParam String string) {
        try {
            return Crypto.decryptString(key, string);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @PostMapping(path = "/encryptdata")
    public byte[] encrypt(@RequestParam String key, @RequestParam("data") MultipartFile data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Crypto.encryptStream(key, data.getInputStream(), outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage().getBytes();
        }
    }

    @PostMapping(path = "/decryptdata")
    public byte[] decrypt(@RequestParam String key, @RequestParam("data") MultipartFile data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Crypto.decryptStream(key, data.getInputStream(), outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage().getBytes();
        }
    }

    @GetMapping(path = "/")
    public String encrypt() {
        return "<html><header></header>" +
                "<body>" +
                "<h2>Welcome to <i>Crypto (ממ״ן תיכנותי 13 עבור הקורס : אבטחת מערכות תוכנה - 22923)</i>.</h2>" +
                "<p><h3>" +
                "This utility enables fast encryption and decryption of strings and files. " +
                "Following are examples of all 4 API functionalities : " +
                "</h3></p>" +
                "<p>" +
                "<h4>Encrypt string :</h4>" +
                "<h5>curl --location --request POST 'http://localhost:8080/encrypt' \n" +
                "--form 'string=some secret text' \n" +
                "--form 'key=1234'</h5>" +
                "<h4>Decrypt string :</h4>" +
                "<h5>curl --location --request POST 'http://localhost:8080/decrypt' \n" +
                "--form 'string=FB269wKDLydQ4P7i4XN+VaTzEhhl' \n" +
                "--form 'key=1234'</h5>" +
                "<h4>Encrypt file :</h4>" +
                "<h5>curl --location --request POST 'http://localhost:8080/encryptdata' \n" +
                "--form 'key=1234' \n" +
                "--form 'data=/temp/secret-data.txt'</h5>" +
                "<h4>Decrypt file :</h4>" +
                "<h5>curl --location --request POST 'http://localhost:8080/decryptdata' \n" +
                "--form 'key=1234' \n" +
                "--form 'data=/temp/secret-data.txt.enc'</h5>" +
                "</p>" +
                "<p><i>" +
                "If you have any questions or need clarifications ,please , contact me : " +
                "<a href='mailto:alexander.bromberg.1973@gmail.com'>" +
                "alexander.bromberg.1973@gmail.com" +
                "</a>" +
                "</i></p>" +
                "</body></html>";
    }


}
