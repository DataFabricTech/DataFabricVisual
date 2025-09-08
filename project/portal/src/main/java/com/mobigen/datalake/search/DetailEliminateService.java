package com.mobigen.datalake.search;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailEliminateService {
    private final DetailEliminateMapper detailEliminateMapper;

    @Value("${properties.trino.hikari.jdbc-url}")
    private String trinoJdbcUrl;

    @Value("${properties.csvFileUploadPath}")
    private String fileUploadPath;

    @Value("${properties.maxFileSize}")
    private String maxFileSize;

    @Value("${properties.bigExtractToken}")
    private String bigExtractToken;

    @Value("${properties.DataExtractAPI.base_url}")
    private String dataExtractApiBaseUrl;

    @Value("${properties.DataExtractAPI.url.largedata}")
    private String dataExtractApiUrlLargedata;

    @Value("${server.ssl.enabled}")
    private Boolean sslEnabled;

    /**
     * 파일 추출 이력 저장
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public Object insertDownloadHistory(Map<String, String> param) throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer insertCount = detailEliminateMapper.insertDownloadHistory(param);
            if (insertCount > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
        }

        return result;
    }

    /**
     * 파일 추출
     * 
     * @param parameter
     * @return
     * @throws SQLException
     */
    public void getCSVFile(Map<String, Object> parameter, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        OutputStream os = response.getOutputStream();

        String filePath = "";
        String encFilePath = "";

        try {
            String query = "";
            if (parameter.get("typeName").toString().endsWith("table")) {
                String[] queryStrSplit = parameter.get("qualified_name").toString().split("\\.");
                String system_id = queryStrSplit[0];
                String dbName = queryStrSplit[1];
                String tableSchema = queryStrSplit[2];
                String tableNameStr = queryStrSplit[3];
                StringBuilder tableName = new StringBuilder();
                tableName.append("\"" + tableNameStr + "\"");
                
                if (parameter.get("system_type") != null && parameter.get("system_type").toString().equalsIgnoreCase("TRINO")) {
                    String catalog = dbName;
                    query = "select " + parameter.get("querySelect").toString() + " from \"" + catalog + "\".\""
                            + tableSchema + "\"." + tableName
                            + parameter.get("queryWhere").toString();// + " limit 10";
                } else {
                    query = "select " + parameter.get("querySelect").toString() + " from \"" + system_id + "\".\""
                            + tableSchema + "\"." + tableName
                            + parameter.get("queryWhere").toString();// + " limit 10";
                }

                if (parameter.get("limit") != null && !parameter.get("limit").toString().equals("")) {
                    query += " LIMIT " + parameter.get("limit").toString();
                }
            }

            Properties properties = new Properties();
            properties.setProperty("user", parameter.get("user_id").toString());
            properties.setProperty("SSL", "false");
            connection = DriverManager.getConnection(trinoJdbcUrl, properties);

            statement = connection.createStatement();

            log.info("file query : " + query);

            rs = statement.executeQuery(query);

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String formattedDateTime = dateFormat.format(currentDate);

            String fileName = parameter.get("fileName") + "_" + formattedDateTime;
            filePath = fileUploadPath + "/" + parameter.get("fileName") + "_" + formattedDateTime + ".csv";

            log.info("만들 파일 경로" + filePath);

            BufferedWriter writer = null;

            try {

                // csv 파일 저장할 폴더 경로 설정
                File csvDir = new File(fileUploadPath);
                if (!csvDir.exists()) { // 해당 폴더 없으면 생성
                    boolean isCreated = csvDir.mkdirs();
                    if (isCreated) {
                        log.info("Success to create new csv directory  => " + fileUploadPath);
                    } else {
                        log.info("Fail to create new csv directory => " + fileUploadPath);
                    }
                } else {
                    log.info("is exists csv directory=> " + fileUploadPath);
                }

                writer = new BufferedWriter(new FileWriter(filePath));
                writer.write("\uFEFF"); // BOM(Byte Order Mark) 작성
            } catch (Exception e) {
                if (writer != null) {
                    writer.close();
                }
                log.error("Error while writing the CSV file: " + e.getMessage());
                throw new Exception("Failed to write CSV file: " + e.getMessage());
            }

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(rs));

            // // 헤더 : name
            // log.info("write header !!");
            // for (int i = 1; i <= columnCount; i++) {
            // csvPrinter.print(rs.getMetaData().getColumnName(i));
            // }
            // csvPrinter.println();

            // 헤더 작성 : userDescription
            // log.info("write userDescription !!");
            // if (parameter.get("userDescriptionList") != null) {
            // Object[] userDescriptionArray = ((List)
            // parameter.get("userDescriptionList")).toArray();
            // if (userDescriptionArray.length > 0) {
            // for (int i = 0; i < userDescriptionArray.length; i++) {
            // csvPrinter.print(userDescriptionArray[i]);
            // }
            // }
            // }
            // log.info("write userDescription end !!");
            // csvPrinter.println();

            // 헤더 작성 : description
            log.info("write description !!");
            if (parameter.get("descriptionList") != null) {
                Object[] descriptionArray = ((List) parameter.get("descriptionList")).toArray();

                if (descriptionArray.length > 0) {
                    for (int i = 0; i < descriptionArray.length; i++) {
                        csvPrinter.print(descriptionArray[i]);
                    }
                }
            }
            log.info("write description end !!");
            csvPrinter.println();

            // 결과를 스트리밍 방식으로 CSV 작성
            log.info("start csv write !!");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    int columnType = metaData.getColumnType(i); // java.sql.Types.XXX
                    Object value = rs.getObject(i);

                    if (value instanceof byte[]) {
                        // VARBINARY, BLOB 등 byte[] 처리
                        csvPrinter.print(bytesToHex((byte[]) value));
                    } else if (columnType == Types.CLOB) {
                        Clob clob = rs.getClob(i);
                        String text = clob.getSubString(1, (int) clob.length());
                        csvPrinter.print(text);
                    } else {
                        // 기본 문자열 처리
                        csvPrinter.print(value);
                    }
                    File f = new File(filePath);
                    long fileSize = f.length();
                    if (fileSize > parseFileSize(maxFileSize)) {
                        csvPrinter.close();

                        log.info("file size : " + fileSize);
                        // 최대 용량을 초과하는 경우
                        log.info("max File Size : " + parseFileSize(maxFileSize));
                        // 파일이 존재하는지 확인
                        if (!f.exists()) {
                            log.info("No Exist File: " + filePath);
                            throw new Exception("No Exist File");
                        }

                        // 파일 삭제 시도
                        boolean deleted = f.delete();
                        log.info("deleted : " + deleted);
                        if (deleted) {
                            log.info("File Delete Success: " + filePath);
                            throw new Exception("The file download capacity has been exceeded.");
                        } else {
                            boolean canWrite = f.canWrite();
                            log.info("canWrite : " + canWrite);
                            if (!canWrite) {
                                log.info("canWrite : " + filePath);
                                throw new Exception("Permission Denied Delete File");
                            }

                            log.info("File Delete Fail: " + filePath);
                            throw new Exception("File Delete Fail");
                        }
                    }
                }
                csvPrinter.println();
                csvPrinter.flush();
            }
            log.info("end csv write !!");

            csvPrinter.flush();
            csvPrinter.close();

            log.info("created csv file path");
            log.info(filePath);
            File file = new File(filePath);

            long fileSize = file.length();
            log.info("file size : " + fileSize);

            try (FileInputStream fis = new FileInputStream(file)) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".csv");
                response.setContentLength((int) fileSize);

                // 파일 내용을 클라이언트에 직접 출력
                log.info("outputStream write start !!");
                byte[] buffer = new byte[4096]; // 4KB 버퍼 생성
                int bytesRead;

                // 파일을 바이트 단위로 읽어서 OutputStream에 씁니다.
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }

                os.flush(); // OutputStream의 버퍼를 강제로 비워서 데이터를 전송합니다.
                // os.close();
                log.info("outputStream write end !!");

                // finally에서 처리
                // rs.close();
                // statement.close();
                // connection.close();
            } catch (Exception e) {
                log.error("Error while processing the CSV file: ", e);
                throw new Exception("Failed to read or send CSV file", e);
            }

        } catch (Exception e) {// 응답 설정
            // 해당 파일이 있는 경우 제거
            if (!filePath.equals("")) {
                // File 객체 생성
                File file = new File(filePath);

                // 파일이 존재하는지 확인하고 삭제
                if (file.exists()) {
                    boolean isDeleted = file.delete();
                    if (isDeleted) {
                        log.info("Success to delete csv file : " + filePath);
                    } else {
                        log.info("Fail to delete csv file : " + filePath);
                    }
                } else {
                    log.info("no exists csv file : " + filePath);
                }
            }
            if (!encFilePath.equals("")) {
                // File 객체 생성
                File file = new File(encFilePath);

                // 파일이 존재하는지 확인하고 삭제
                if (file.exists()) {
                    boolean isDeleted = file.delete();
                    if (isDeleted) {
                        log.info("Success to delete enc csv file : " + encFilePath);
                    } else {
                        log.info("Fail to delete enc csv file : " + encFilePath);
                    }
                } else {
                    log.info("no exists enc csv file : " + encFilePath);
                }
            }

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("errorMessage", "");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (e.getMessage().indexOf("Access Denied") != -1) { // 권한이 없는 경우
                log.info(e.getMessage());
                String errorMessage = "파일 추출 권한이 없습니다.";
                responseMap.put("errorMessage", errorMessage);
                String errorResult = convertMapToJson(responseMap);
                response.getOutputStream().write(errorResult.getBytes("UTF-8"));
                response.getOutputStream().flush();
                throw new Exception(errorMessage);
            } else if (e.getMessage().indexOf("Column") != -1 ||
                    e.getMessage().indexOf("mismatched input") != -1 ||
                    e.getMessage().indexOf("Unknown resolvedType:") != -1 ||
                    e.getMessage().indexOf("Query failed") != -1) {
                log.info(e.getMessage());
                String errorMessage = "쿼리가 잘못되었습니다.";
                responseMap.put("errorMessage", errorMessage);
                String errorResult = convertMapToJson(responseMap);
                response.getOutputStream().write(errorResult.getBytes("UTF-8"));
                response.getOutputStream().flush();
                throw new Exception(errorMessage);
            } else if (e.getMessage().indexOf("OutOfMemoryError") != -1) { // 용량이 너무 큰 경우
                log.info(e.getMessage());
                String errorMessage = "용량이 너무 커서 다운로드 받을 수 없습니다.";
                responseMap.put("errorMessage", errorMessage);
                String errorResult = convertMapToJson(responseMap);
                response.getOutputStream().write(errorResult.getBytes("UTF-8"));
                response.getOutputStream().flush();
                throw new Exception(errorMessage);
            } else if (e.getMessage().indexOf("Iceberg") != -1) {
                log.info(e.getMessage());
                String errorMessage = "Iceberg 테이블입니다.";
                responseMap.put("errorMessage", errorMessage);
                String errorResult = convertMapToJson(responseMap);
                response.getOutputStream().write(errorResult.getBytes("UTF-8"));
                response.getOutputStream().flush();
                throw new Exception(errorMessage);
            } else if (e.getMessage().indexOf("The file download capacity has been exceeded") != -1) {
                log.info(e.getMessage());
                String errorMessage = "용량이 너무 커서 다운로드에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.";
                responseMap.put("errorMessage", errorMessage);
                String errorResult = convertMapToJson(responseMap);
                response.getOutputStream().write(errorResult.getBytes("UTF-8"));
                response.getOutputStream().flush();
                throw new Exception(errorMessage);
            } else {
                e.printStackTrace();
                log.info(e.getMessage());
                String errorMessage = "파일 다운로드에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.";
                responseMap.put("errorMessage", errorMessage);
                String errorResult = convertMapToJson(responseMap);
                response.getOutputStream().write(errorResult.getBytes("UTF-8"));
                response.getOutputStream().flush();
                throw new Exception(errorMessage);
            }
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                }
            if (statement != null)
                try {
                    statement.close();
                } catch (Exception e) {
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {
                }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        // sb.append("0x");
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    // 대용량 다운로드 요청
    public Object downloadRequest(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        params.put("token", BCryptEncoder(bigExtractToken)); // 대용량 데이터 추출 토큰
        try {

            Map<String, Object> restResponse = bigExtractRequestApi(params);
            if (!restResponse.isEmpty()) {
                Map<String, String> res = (Map<String, String>) restResponse.get("result");
                if (res.get("result").equals("success")) {
                    result.put("result", true);
                } else {
                    result.put("result", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("대용량 다운로드 요청 실패");
            result.put("result", false);
        }
        return result;
    }

    // 파일 최대 크기 반환
    public static long parseFileSize(String sizeStr) {
        sizeStr = sizeStr.toUpperCase().trim();

        long multiplier = 1;
        if (sizeStr.endsWith("KB")) {
            multiplier = 1024;
            sizeStr = sizeStr.replace("KB", "").trim();
        } else if (sizeStr.endsWith("MB")) {
            multiplier = 1024 * 1024;
            sizeStr = sizeStr.replace("MB", "").trim();
        } else if (sizeStr.endsWith("GB")) {
            multiplier = 1024 * 1024 * 1024;
            sizeStr = sizeStr.replace("GB", "").trim();
        }

        return Long.parseLong(sizeStr) * multiplier;
    }

    // Map을 JSON 문자열로 변환하는 메서드
    private String convertMapToJson(Map<String, String> map) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            jsonBuilder.append("\"").append(entry.getKey()).append("\": ")
                    .append("\"").append(entry.getValue()).append("\", ");
        }

        // 마지막 쉼표 제거
        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 2);
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    // 대용량 데이터 추출 토큰 인코딩용
    public String BCryptEncoder(String value) throws Exception {
        String token = "";
        try {

            if (value != null && !value.equals("")) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                // 비밀번호 해싱
                token = passwordEncoder.encode(value);
            }
        } catch (Exception e) {
            log.error("대용량 데이터 추출 토큰 인코딩 실패");
        }
        return token;
    }

    public Map<String, Object> bigExtractRequestApi(Map<String, Object> requestBody) throws Exception {
        // 모든 SSL 인증서를 신뢰하는 SSLContext 생성
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                .build();

        // SSL 연결 시 호스트명 검증을 무시하는 SSLConnectionSocketFactory 생성
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        // http와 https에 사용할 ConnectionSocketFactory 등록
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        // 커넥션 매니저 생성
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        // 커넥션 매니저를 사용하여 HttpClient 생성
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        // HttpClient를 사용하여 RestTemplate 생성
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        String url = dataExtractApiBaseUrl + dataExtractApiUrlLargedata;
        Map<String, Object> restResponse = restTemplate.postForObject(url, requestBody, HashMap.class);
        return restResponse;
    }

    public Map<String, Object> getIsSslEnabled() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("isSslEnabled", sslEnabled);
        return result;
    }
}
