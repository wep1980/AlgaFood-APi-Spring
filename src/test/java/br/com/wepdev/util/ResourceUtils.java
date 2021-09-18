package br.com.wepdev.util;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ResourceUtils {


    /**
     * Metodo que ler um arquivo .json
     * @param resourceName -> nome do aquivo.json
     * @return
     */
    public static String getContentFromResource(String resourceName) {
        try {
            InputStream stream = ResourceUtils.class.getResourceAsStream(resourceName);
            return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
