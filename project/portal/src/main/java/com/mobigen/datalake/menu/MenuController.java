package com.mobigen.datalake.menu;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping("/api")
@Controller
@AllArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/images")
    public ResponseEntity<byte[]> getImage(@RequestParam String path) {
        try {
            // 파일 경로를 Path 객체로 변환
            Path imagePath = Paths.get(path).toAbsolutePath().normalize();
            File imageFile = imagePath.toFile();

            // 파일 존재 여부 확인
            if (!imageFile.exists()) {
                String message = "파일을 찾을 수 없습니다: " + path;
                return ResponseEntity.ok(message.getBytes()); // 메시지를 바이트 배열로 변환하여 반환
            }

            // 파일 확장자에 따른 MIME 타입 설정
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // 기본 MIME 타입 설정
            }

            // 파일을 바이트 배열로 읽기
            byte[] imageBytes;

            try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
                imageBytes = fileInputStream.readAllBytes();
            } catch (IOException e) {
                log.error("Error while reading the image file: ", e);
                throw new RuntimeException("Failed to read image file", e);
            }

            // ResponseEntity에 MIME 타입과 파일 데이터 설정
            ResponseEntity<byte[]> result = ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageFile.getName() + "\"")
                    .body(imageBytes);
            return result;
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ResponseJsonResult
    @GetMapping("/menu")
    public Object getMenu(HttpServletRequest request) throws Exception {
        return menuService.getMenu(request);
    }

    @ResponseJsonResult
    @PostMapping("/log")
    public void postLog(HttpServletRequest request) throws Exception {
        log.info("No static Resource fix : '/api/log'");
    }

    @ResponseJsonResult
    @GetMapping("/menu/click")
    public Object clickMenu(@RequestParam String menuID, @RequestParam String userID,
            @RequestParam String deptID) throws Exception {
        return menuService.clickMenu(userID, menuID, deptID);
    }

    @ResponseJsonResult
    @GetMapping("/menu/history")
    public Object getMenuHistory() throws Exception {
        return menuService.getMenuHistory();
    }

    @ResponseJsonResult
    @GetMapping("/roleMenu")
    public Object getRoleMenu(@RequestParam String roleType) throws Exception {
        return menuService.getRoleMenu(roleType);
    }

    @ResponseJsonResult
    @PostMapping("/roleMenuSave")
    @ResponseBody
    public Object roleMenuSave(@RequestBody Map<String, Object> param) throws Exception {
        return menuService.roleMenuSave(param);
    }

}
