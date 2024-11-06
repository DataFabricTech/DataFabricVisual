package com.mobigen.framework.utility;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import com.duruan.shadowcube.SecureStreamCreator;
import com.duruan.shadowcube.SecureStreamReader;

import org.springframework.beans.factory.annotation.Value;

public class FileEncDecUtil {
    private static SecureStreamCreator ssc = null;

    @Value("${shadowcube.serial-id}")
    private static String SERIAL_ID;

    @Value("${shadowcube.password}")
    private static String PASSWORD;

    @Value("${shadowcube.license}")
    private static String LICENSE;

    FileEncDecUtil() {}

    private static class Holder {
        private static final FileEncDecUtil INSTANCE = new FileEncDecUtil();
    }

    // getInstance
    private static FileEncDecUtil getInstance() {
        return Holder.INSTANCE;
    }

    // Encode
    public static synchronized void encode(String input, String output) throws Exception {
        SecureStreamReader ssr = getInstance().ssc.CreateEncryptReader(input, 0, 0);
        ssr.ReadAllToFile(output, null);
        ssr.Close();
        System.out.println("!!! Finished Encrypt : " + input + "\n\t->" + output);
    }

    // Decode
    public static synchronized void decode(String input, String output) throws Exception {
        SecureStreamReader ssr = getInstance().ssc.CreateDecryptReader(input);
        if (ssr != null) {
            try (FileOutputStream out = new FileOutputStream(output)) {
                byte[] buff = new byte[2048];
                while(ssr.Available() > 0) {
                    long read = ssr.Read (buff, 2048);
                    out.write(buff, 0, (int) read);
                }
                out.flush(); // 데이터를 강제로 디스크에 씁니다.
            } catch (Exception e) {
                System.out.println("Error while writing to output file: " + e);
                throw new RuntimeException("Failed to write to file", e);
            } finally {
                ssr.Close(); // SecureStreamReader 자원 닫기
            }
        }

        System.out.println("!!! Finished Decrypt : " + input + "\n\t->" + output);
    }

    // public static SecureStreamCreator getSecureStreamCreator() throws Exception {
    //     if (_secureStreamCreator != null) return _secureStreamCreator;

    //     SecureStreamCreator.Init(SERIAL_ID, PASSWORD, LICENSE);

    //     _secureStreamCreator = new SecureStreamCreator();

    //     return _secureStreamCreator;
    // }

}