package com.lamechat.service;

import java.io.*;

public class ServiceUtils {

    public static String loadFileAsString(String str) {

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ServiceUtils.class.getClassLoader().getResourceAsStream(str)))) {
            while (reader.ready()) {
                builder.append(reader.readLine());
                builder.append("\n");
            }
        } catch (IOException e) {
            return null;
        }
        return builder.toString();
    }

    public static byte[] loadFileAsByteArray(String str) {
        try (InputStream r = ServiceUtils.class.getClassLoader().getResourceAsStream(str)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4096];
            while ((nRead = r.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
