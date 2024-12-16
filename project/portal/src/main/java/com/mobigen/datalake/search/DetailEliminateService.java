package com.mobigen.datalake.search;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mobigen.framework.security.SessionManager;

import io.trino.jdbc.$internal.joda.time.LocalDateTime;
import io.trino.jdbc.$internal.joda.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailEliminateService {
    private final DetailEliminateMapper detailEliminateMapper;

    private final SessionManager sessionManager;

    @Value("${properties.trino.hikari.jdbc-url}")
    private String trinoJdbcUrl;

    @Value("${properties.csvFileUploadPath}")
    private String fileUploadPath;

    @Value("${properties.maxFileSize}")
    private String maxFileSize;

    /**
     * 파일 추출 이력 저장
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public Object insertDownloadHistory(Map<String, String> param) throws Exception {
        Integer result = detailEliminateMapper.insertDownloadHistory(param);

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
            // hive_column
            if (parameter.get("typeName").toString().endsWith("column")) {
                String queryStrSplit[] = parameter.get("qualifiedName").toString().split("@");
                String queryStr = queryStrSplit[0];
                String queryStrname[] = queryStr.split("\\.");
                StringBuilder tableName = new StringBuilder();
                for (int i = 0; i < queryStrname.length - 1; i++) {
                    if (!tableName.toString().equals("")) {
                        tableName.append(".").append("\"" + queryStrname[i] + "\"");
                    } else {
                        tableName.append("\"" + queryStrname[i] + "\"");
                    }
                }
                query = "select " + parameter.get("querySelect").toString() + " from " + tableName
                        + parameter.get("queryWhere").toString();// + " limit 10";
                if (parameter.get("limit") != null && !parameter.get("limit").toString().equals("")) {
                    query += " LIMIT " + parameter.get("limit").toString();
                }
            }

            // hive_table
            if (parameter.get("typeName").toString().endsWith("table")) {
                String[] queryStrSplit = parameter.get("qualifiedName").toString().split("\\.");
                String catalog = queryStrSplit[0];
                String dbName = queryStrSplit[1];
                String tableSchema = queryStrSplit[2];
                String tableNameStr = queryStrSplit[3];
                String[] tableNameArr = tableNameStr.split("\\.");
                StringBuilder tableName = new StringBuilder();
                if (tableNameArr.length > 1) {
                    for (int i = 0; i < tableNameArr.length; i++) {
                        if (!tableName.toString().equals("")) {
                            tableName.append(".").append("\"" + tableNameArr[i] + "\"");
                        } else {
                            tableName.append("\"" + tableNameArr[i] + "\"");
                        }
                    }
                } else {
                    tableName.append("\"" + tableNameStr + "\"");
                }

                query = "select " + parameter.get("querySelect").toString() + " from \"" + catalog + "\".\"" + tableSchema + "\"." + tableName
                        + parameter.get("queryWhere").toString();// + " limit 10";
                        
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

            int columnCount = rs.getMetaData().getColumnCount();

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String formattedDateTime = dateFormat.format(currentDate);

            String fileName = parameter.get("fileName") + "_" + formattedDateTime;
            filePath = fileUploadPath + "/" + parameter.get("fileName") + "_" + formattedDateTime + ".csv";

            log.info("만들 파일 경로"+ filePath);

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
                if (writer != null){
                    writer.close();
                }
                log.error("Error while writing the CSV file: " + e.getMessage());
                throw new Exception("Failed to write CSV file: " +  e.getMessage()); 
            } 

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(rs));
            
            // // 헤더 : name
            // log.info("write header !!");
            // for (int i = 1; i <= columnCount; i++) {
            //     csvPrinter.print(rs.getMetaData().getColumnName(i));
            // }
            // csvPrinter.println();

            // 헤더 작성 : userDescription
            // log.info("write userDescription !!");
            // if (parameter.get("userDescriptionList") != null) {
            //     Object[] userDescriptionArray = ((List) parameter.get("userDescriptionList")).toArray();
            //     if (userDescriptionArray.length > 0) {
            //         for (int i = 0; i < userDescriptionArray.length; i++) {
            //             csvPrinter.print(userDescriptionArray[i]);
            //         }
            //     }
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
                    csvPrinter.print(rs.getObject(i));
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

}
