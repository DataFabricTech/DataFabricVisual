package com.mobigen.datalake.metaData.standard;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.metaData.standard.type.DataType;
import com.mobigen.datalake.metaData.standard.util.ExcelUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class MetaDataStandardService {
    private final MetaDataStandardMapper metaDataStandardMapper;

    // 표준 데이터 목록 조회
    public Map<String, Object> getMetaDataStandardList(Map<String, Object> param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        int total = 0;
        List<Map<String, Object>> list = new ArrayList<>();
        
        try {
            // 표준 용어, 표준 단어, 표준 도메인
            String select_type = param.get("select_type").toString();
            if (select_type.equals("term")) {
                list = metaDataStandardMapper.getMetaDataStandardTermList(param);
                total = metaDataStandardMapper.getMetaDataStandardTermListCount(param);
            } else if (select_type.equals("word")) {
                list = metaDataStandardMapper.getMetaDataStandardWordList(param);
                total = metaDataStandardMapper.getMetaDataStandardWordListCount(param);
            } else if (select_type.equals("domain")) {
                list = metaDataStandardMapper.getMetaDataStandardDomainList(param);
                total = metaDataStandardMapper.getMetaDataStandardDomainListCount(param);
            }             

            resultMap.put("list", list);
            resultMap.put("total", total);
        } catch (Exception e) {
            log.error("getMetaDataStandardList Failed !!");
            resultMap.put("list", new ArrayList<>());
            resultMap.put("total", 0);
        }

        return resultMap;
    }

    // 표준 데이터 정보 조회
    public Map<String, Object> getMetaDataStandardInfo(String id, String select_type) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        boolean result = false;
        Map<String, Object> info = new HashMap<>();

        try {
            // 표준 용어, 표준 단어, 표준 도메인
            if (select_type.equals("term")) {
                info = metaDataStandardMapper.getMetaDataStandardTermInfo(id);
            } else if (select_type.equals("word")) {
                info = metaDataStandardMapper.getMetaDataStandardWordInfo(id);
            } else if (select_type.equals("domain")) {
                info = metaDataStandardMapper.getMetaDataStandardDomainInfo(id);
            }

            if (info != null && !info.isEmpty()) {
                result = true;
            }
            resultMap.put("result", result);
            resultMap.put("info", info);
            
        } catch(Exception e) {
            resultMap.put("result", false);
            resultMap.put("info", new HashMap<>());
        }

        return resultMap;
    }
    
    // 표준 데이터 삭제 처리 (used = 'N')
    public Map<String, Object> deleteSoft(String id, String select_type) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Integer deleteResult = 0;
        try {
            // 표준 용어
            if (select_type.equals("term")) { deleteResult = metaDataStandardMapper.deleteSoftTerm(id); }
            // 표준 단어
            if (select_type.equals("word")) { deleteResult = metaDataStandardMapper.deleteSoftWord(id); }
            // 표준 도메인
            if (select_type.equals("domain")) { deleteResult = metaDataStandardMapper.deleteSoftDomain(id); }

            // 삭제 결과 반환
            if (deleteResult != null && deleteResult > 0) {
                result.put("deleteResult", true);
            } else {
                result.put("deleteResult", false);
            }
        } catch (Exception e) {
            log.error("deleteSoft Failed !!");
            result.put("deleteResult", false);
        }

        return result;
    }

    // 용어명 중복확인
    public Map<String, Object> checkTermDuplicated(String word, String type) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean duplicated = false;
        try {
            switch (type) {
                case "kor": {
                    Integer dupCount = metaDataStandardMapper.existsByKorName(word);
                    if (dupCount != null && dupCount > 0) {
                        duplicated = true;
                    }
                    result.put("duplicated", duplicated);
                    result.put("duplicatedResult", true);
                    return result;
                }
                case "eng_sign": {
                    Integer dupCount = metaDataStandardMapper.existsByEnglishSign(word);
                    if (dupCount != null && dupCount > 0) {
                        duplicated = true;
                    }
                    result.put("duplicated", duplicated);
                    result.put("duplicatedResult", true);
                    return result;
                }
                default: {
                    result.put("duplicated", false);
                    result.put("duplicatedResult", false);
                    return result;
                }
            }
        } catch (Exception e) {
            log.error("checkTermDuplicated Failed..");
            result.put("duplicated", false);
            result.put("duplicatedResult", false);
        }

        return result;
    }

    // 표준단어 중복확인
    public Map<String, Object> checkWordDuplicated(String word, String type) {
        Map<String, Object> result = new HashMap<>();
        boolean duplicated = false;
        try {
            switch (type) {
                case "kor": {
                    Integer dupCount = metaDataStandardMapper.existsByKorNameFromWord(word);
                    if (dupCount != null && dupCount > 0) {
                        duplicated = true;
                    }
                    result.put("duplicated", duplicated);
                    result.put("duplicatedResult", true);
                    return result;
                }
                case "eng_sign": {
                    Integer dupCount = metaDataStandardMapper.existsByEnglishSignFromWord(word);
                    if (dupCount != null && dupCount > 0) {
                        duplicated = true;
                    }
                    result.put("duplicated", duplicated);
                    result.put("duplicatedResult", true);
                    return result;
                }
                case "eng": {
                    Integer dupCount = metaDataStandardMapper.existsByEnglishFromWord(word);
                    if (dupCount != null && dupCount > 0) {
                        duplicated = true;
                    }
                    result.put("duplicated", duplicated);
                    result.put("duplicatedResult", true);
                    return result;
                }
                default: {
                    result.put("duplicated", false);
                    result.put("duplicatedResult", false);
                    return result;
                }
            }
        } catch (Exception e) {
            log.error("checkWordDuplicated Failed..");
            result.put("duplicated", false);
            result.put("duplicatedResult", false);
        }

        return result;
    }

    // 표준 도메인 중복확인
    public Map<String, Object> checkDomainDuplicated(String word, String type) {
        Map<String, Object> result = new HashMap<>();
        boolean duplicated = false;
        try {
            switch (type) {
                case "kor": {
                    Integer dupCount = metaDataStandardMapper.existsByKorNameFromDomain(word);
                    if (dupCount != null && dupCount > 0) {
                        duplicated = true;
                    }
                    result.put("duplicated", duplicated);
                    result.put("duplicatedResult", true);
                    return result;
                }
                default: {
                    result.put("duplicated", false);
                    result.put("duplicatedResult", false);
                    return result;
                }
            }
        } catch (Exception e) {
            log.error("checkDomainDuplicated Failed..");
            result.put("duplicated", false);
            result.put("duplicatedResult", false);
        }

        return result;
    }


    // 표준용어 등록
    public Map<String, Object> createTerm(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 표준용어 등록
            Integer createResult = metaDataStandardMapper.createTerm(params);
            
            // 2. 도메인 등록
            // 도메인 명으로 도메인 생성
            Map<String, Object> domainParams = this.makeDomainParamByDomainName(params.get("domain").toString());
            domainParams.put("creator", params.get("creator").toString()); // 생성자 ID
            Integer createDomainResult = metaDataStandardMapper.createTermDomain(domainParams);

            if (createResult != null && createResult > 0 && createDomainResult != null && createDomainResult > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
        }
        
        return result;
    }

    // 도메인 명으로 도메인 파라미터 생성
	private Map<String, Object> makeDomainParamByDomainName(String domainName) throws Exception {
        Map<String, Object> domainParmas = new HashMap<>();
		Pattern pattern = Pattern.compile("^([가-힣]+)([A-Za-z])(.+)?$");
		Matcher matcher = pattern.matcher(domainName);
		if (!matcher.matches()) {
			throw new Exception("Fail To Create Domain !!");
		}
		String domainClassifiedName = matcher.group(1);
		DataType dataType = DataType.getDataTypeBySign(matcher.group(2));
		Long dataSize = null;
		Long decimalPointSize = null;
		String dataSizeString = matcher.group(3);
		if (dataSizeString != null) {
			if (dataSizeString.contains(",")) {
				String[] split = dataSizeString.split(",");
				dataSize = Long.parseLong(split[0]);
				decimalPointSize = Long.parseLong(split[1]);
			} else {
				dataSize = Long.parseLong(dataSizeString);
			}
		}

        String groupName = "";
        String description = "";
        Map<String, Object> domainInfo = metaDataStandardMapper.getDomainInfoByDomainClassifiedName(domainClassifiedName);
        if (domainInfo != null) {
            groupName = domainInfo.get("group_name") != null ? domainInfo.get("group_name").toString() : "";
            description = domainInfo.get("description") != null ? domainInfo.get("description").toString() : "";
        }

        domainParmas.put("id", UUID.randomUUID());
        domainParmas.put("name", domainName);
        domainParmas.put("group_name", groupName);
        domainParmas.put("domain_classified_name", domainClassifiedName);
        domainParmas.put("description", description);
        domainParmas.put("data_type", dataType);
        domainParmas.put("data_size", dataSize);
        domainParmas.put("decimal_point_size", decimalPointSize);
        domainParmas.put("used", "Y");

        return domainParmas;
    }

    // 표준용어 수정
    public Map<String, Object> updateTerm(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer updateResult = metaDataStandardMapper.updateTerm(params);
            if (updateResult != null && updateResult > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
        }
        
        return result;
    }

    // 표준용어 정보 수정
    public Map<String, Object> updateTermInfo(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer updateResult = metaDataStandardMapper.updateTermInfo(params);
            if (updateResult != null && updateResult > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
        }
        
        return result;
    }

    // 표준용어 도메인 수정
    @Transactional
    public Map<String, Object> updateTermDomain(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Integer updateResult = 0;
        Integer domainUpdateResult = 0;
        Integer domainCreateResult = 0;

        try {
            // 1. 용어의 도메인 내용 수정
            updateResult = metaDataStandardMapper.updateTermDomain(params);

            // 2. 수정하면서 생성된 도메인 등록
            // 2-1. 동일한 도메인 명 존재하면 그대로 두기
            Integer dupCnt = 0;
            String origin_domain = params.get("origin_domain") != null ? params.get("origin_domain").toString() : "";
            if (!origin_domain.equals("")) {
                dupCnt = metaDataStandardMapper.dupCheckDomainName(origin_domain);
                if (dupCnt != null && dupCnt > 0) {
                } else { // 신규 등록
                    Map<String, Object> domainParams = this.makeDomainParamByDomainName(params.get("domain").toString());
                    domainParams.put("creator", params.get("creator").toString()); // 생성자 ID
                    domainCreateResult = metaDataStandardMapper.createTermDomain(domainParams);
                }
            } else { // 2-2. 신규 도메인명은 신규 추가
                Map<String, Object> domainParams = this.makeDomainParamByDomainName(params.get("domain").toString());
                domainParams.put("creator", params.get("creator").toString()); // 생성자 ID
                domainCreateResult = metaDataStandardMapper.createTermDomain(domainParams);
            }

            if ((updateResult != null && updateResult > 0)
            && ((domainUpdateResult != null && domainUpdateResult > 0) || (domainCreateResult != null && domainCreateResult > 0))) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
            throw e;
        }
        
        return result;
    }

    // 표준단어 등록
    public Map<String, Object> createWord(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer createResult = metaDataStandardMapper.createWord(params);
            if (createResult != null && createResult > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
        }
        
        return result;
    }

    // 표준단어 수정
    public Map<String, Object> updateWord(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer updateResult = metaDataStandardMapper.updateWord(params);
            if (updateResult != null && updateResult > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
        }
        
        return result;
    }

    // 표준 도메인 등록
    public Map<String, Object> createDomain(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer createResult = metaDataStandardMapper.createDomain(params);
            if (createResult != null && createResult > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
        }
        
        return result;
    }

    // 표준 도메인 수정
    public Map<String, Object> updateDomain(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            Integer updateResult = metaDataStandardMapper.updateDomain(params);
            if (updateResult != null && updateResult > 0) {
                result.put("result", true);
            } else {
                result.put("result", false);
            }
        } catch (Exception e) {
            result.put("result", false);
        }
        
        return result;
    }

    public Map<String, Object> uploadExcel(MultipartFile file, Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Integer insertCount = 0;
        boolean result = false;
        String resultMessage = "";
        try (InputStream is = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);) {
            
            Sheet sheet = workbook.getSheetAt(0);
    
            List<Map<String, Object>> dataList = new ArrayList<>();
            // 표준 용어
            if (params.get("search_type") != null && params.get("search_type").equals("term")) {
                dataList = this.excelRowsToTerm(sheet.iterator(), params);
                if (dataList != null && dataList.size() > 0) {
                    insertCount = metaDataStandardMapper.insertStandardTerms(dataList);
                }
            }
            // 표준 단어
            if (params.get("search_type") != null && params.get("search_type").equals("word")) {
                dataList = this.excelRowsToWord(sheet.iterator(), params);
                if (dataList != null && dataList.size() > 0) {
                    insertCount = metaDataStandardMapper.insertStandardWords(dataList);
                }
            }
            // 표준 도메인
            if (params.get("search_type") != null && params.get("search_type").equals("domain")) {
                dataList = this.excelRowsToDomain(sheet.iterator(), params);
                if (dataList != null && dataList.size() > 0) {
                    insertCount = metaDataStandardMapper.insertStandardDomains(dataList);
                }
            }

            if (insertCount != null && insertCount > 0) {
                result = true;
            } else {
                resultMessage = "저장된 데이터가 없습니다.";
                result = false;
            }

            resultMap.put("resultMessage", resultMessage);
            resultMap.put("insertCount", insertCount);
            resultMap.put("result", result);
        } catch(Exception e) {
            log.error("excel upload error !!");
            resultMessage = e.getMessage();
            resultMap.put("resultMessage", resultMessage);
            resultMap.put("insertCount", insertCount);
            resultMap.put("result", result);
        }

        return resultMap;
    }

    private List<Map<String, Object>> excelRowsToTerm(Iterator<Row> rowIterator, Map<String, Object> params) throws Exception {
		// 중복 체크용
		List<Map<String, Object>> allTerms = metaDataStandardMapper.getAllTermsForDuplicateCheck();
		List<String> allDomainNames = metaDataStandardMapper.getAllDomainNames();

		// 신규 생성 list
		List<Map<String, Object>> termList = new ArrayList<>();

        // 등록사용자
        String creator = params.get("creator").toString();
        String modifier = params.get("modifier").toString();

		int lineNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			lineNumber++;

			// 헤더, 1번행 패스
			if (lineNumber < 2) {
				continue;
			}
			if (ExcelUtils.isRowEmpty(row)) {
				break;
			}
            Map<String, Object> term = new HashMap<>();
			

            String id = UUID.randomUUID().toString();
            String final_version_date = ExcelUtils.getStringCellValue(row.getCell(1), false);
			if (final_version_date.isEmpty()) {
				final_version_date = LocalDate.now().toString();
			}
			String name = ExcelUtils.getStringCellValue(row.getCell(2), false);
			String description = ExcelUtils.getStringCellValue(row.getCell(3), false);
			String english_sign = ExcelUtils.getStringCellValue(row.getCell(4), false);
			String domain = ExcelUtils.getStringCellValue(row.getCell(5), true);
			String permission_value = ExcelUtils.getStringCellValue(row.getCell(6), true);
			String save_format = ExcelUtils.getStringCellValue(row.getCell(7), true);
			String expression_format = ExcelUtils.getStringCellValue(row.getCell(8), true);
			String admin_standard_code_name = ExcelUtils.getStringCellValue(row.getCell(9), true);
			String competent_authority = ExcelUtils.getStringCellValue(row.getCell(10), true);
			String synonym = ExcelUtils.getStringCellValue(row.getCell(11), true);
            String used = "Y";

            term.put("id", id);
            term.put("name", name);
            term.put("description", description);
            term.put("english_sign", english_sign);
            term.put("domain", domain);
            term.put("permission_value", permission_value);
            term.put("save_format", save_format);
            term.put("expression_format", expression_format);
            term.put("admin_standard_code_name", admin_standard_code_name);
            term.put("competent_authority", competent_authority);
            term.put("synonym", synonym);
            term.put("used", used);
            term.put("final_version_date", final_version_date);
            term.put("creator", creator);
            term.put("modifier", modifier);

			// 중복 validation
			Map<String, Object> validateResult = validateTerm(allTerms, allDomainNames, term);
			if (!(boolean) validateResult.get("validateResult")) {
				throw new Exception(validateResult.get("validateResultMessage").toString());
			}

			termList.add(term);
		}
		return termList;
	}
    
    public Map<String, Object> validateTerm(List<Map<String, Object>> allTerms, List<String> allDomainNames, Map<String, Object> term) {
        Map<String, Object> validateResultMap = new HashMap<>();
        boolean validateResult = false;
        String validateResultMessage = "";

        try {
            Set<String> allTermNames = new HashSet<>();
            Set<String> allTermEnglishSigns = new HashSet<>();;
            Set<String> domainNames = new HashSet<>(allDomainNames);
            for (Map<String, Object> item : allTerms) {
                allTermNames.add(item.get("name").toString());
                allTermEnglishSigns.add(item.get("english_sign").toString());
            }

            if (!allTermNames.add(term.get("name").toString())) {
                validateResultMessage = "중복된 용어명이 있습니다. - " + "(" + term.get("name") + ")";
            } else if (!allTermEnglishSigns.add(term.get("english_sign").toString())) {
                validateResultMessage = "중복된 영문약어명이 있습니다. - " + "(" + term.get("english_sign") + ")";
            } else {
                if (!domainNames.contains(term.get("domain").toString())) {
                    validateResultMessage = "도메인명이 존재하지 않습니다. - " + "(" + term.get("domain") + ")";
                } else {
                    validateResult = true;
                }
            }
        } catch(Exception e) {
            validateResult = false;
            validateResultMessage = "용어 유효성 검사에 실패하였습니다.";
            log.error(validateResultMessage);
        }
        
        validateResultMap.put("validateResult", validateResult);
        validateResultMap.put("validateResultMessage", validateResultMessage);

        return validateResultMap;
    }

    private List<Map<String, Object>> excelRowsToWord(Iterator<Row> rowIterator, Map<String, Object> params) throws Exception {
		// 중복 체크용
		List<Map<String, Object>> allWords = metaDataStandardMapper.getAllWordsForDuplicateCheck();

		//신규 생성
		List<Map<String, Object>> wordList = new ArrayList<>();

		int lineNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			lineNumber++;

			// 헤더, 1번행 패스
			if (lineNumber < 2) {
				continue;
			}
			if (ExcelUtils.isRowEmpty(row)) {
				break;
			}
            Map<String, Object> word = new HashMap<>();
			
            String id = UUID.randomUUID().toString();
            String final_version_date = ExcelUtils.getStringCellValue(row.getCell(1), false);
			if (final_version_date.isEmpty()) {
				final_version_date = LocalDate.now().toString();
			}
			String name = ExcelUtils.getStringCellValue(row.getCell(2), false);
			String english_sign = ExcelUtils.getStringCellValue(row.getCell(3), false);
			String word_eng = ExcelUtils.getStringCellValue(row.getCell(4), false);
			String description = ExcelUtils.getStringCellValue(row.getCell(5), false);
			String form_checked = ExcelUtils.getStringCellValue(row.getCell(6), true);
			String domain_classified_name = ExcelUtils.getStringCellValue(row.getCell(7), true);
			String synonym = ExcelUtils.getStringCellValue(row.getCell(8), true);
			String forbidden = ExcelUtils.getStringCellValue(row.getCell(9), true);
            String used = "Y";

            word.put("id", id);
            word.put("final_version_date", final_version_date);
            word.put("name", name);
            word.put("english_sign", english_sign);
            word.put("word_eng", word_eng);
            word.put("description", description);
            word.put("form_checked", form_checked);
            word.put("domain_classified_name", domain_classified_name);
            word.put("synonym", synonym);
            word.put("forbidden", forbidden);
            word.put("used", used);

            // 중복 validation
			Map<String, Object> validateResult = validateWord(allWords, word);
			if (!(boolean) validateResult.get("validateResult")) {
				throw new Exception(validateResult.get("validateResultMessage").toString());
			}

			wordList.add(word);
		}
		return wordList;
	}

    public Map<String, Object> validateWord(List<Map<String, Object>> allWords, Map<String, Object> word) {
        Map<String, Object> validateResultMap = new HashMap<>();
        boolean validateResult = false;
        String validateResultMessage = "";

        try {
            Set<String> allWordNames = new HashSet<>();
            Set<String> allWordEnglishSigns = new HashSet<>();
            for (Map<String, Object> item : allWords) {
                allWordNames.add(item.get("name").toString());
                allWordEnglishSigns.add(item.get("english_sign").toString());
            }

            if (!allWordNames.add(word.get("name").toString())) {
                validateResultMessage = "중복된 단어명이 있습니다. - " + "(" + word.get("name") + ")";
            } else if (!allWordEnglishSigns.add(word.get("english_sign").toString())) {
                validateResultMessage = "중복된 영문약어명이 있습니다. - " + "(" + word.get("english_sign") + ")";
            } else {
                // 형식단어인데, 도메인 분류어가 비어있는 경우
                if (word.get("form_checked").toString().equals("Y")) {
                    if (word.get("domain_classified_name") != null && !word.get("domain_classified_name").toString().equals("")) {
                        validateResult = true;
                    } else {
                        validateResultMessage = "형식단어는 도메인 분류어가 필수로 입력되어야합니다. - " + "(" + word.get("name") + ")";
                    }
                } else {
                    validateResult = true;
                }
            }
        } catch(Exception e) {
            validateResult = false;
            validateResultMessage = "단어 유효성 검사에 실패하였습니다.";
            log.error(validateResultMessage);
        }
        
        validateResultMap.put("validateResult", validateResult);
        validateResultMap.put("validateResultMessage", validateResultMessage);
        
        return validateResultMap;
    }

    private List<Map<String, Object>> excelRowsToDomain(Iterator<Row> rowIterator, Map<String, Object> params) throws Exception {
		// 중복 체크용
		List<String> allDomainNames = metaDataStandardMapper.getAllDomainNames();
        Map<String, String> dataTypeMap = new HashMap<>(); // key: dataType, value: 약어
        for (DataType dataType : DataType.values()) {
            dataTypeMap.put(dataType.name(), dataType.getSign());
        }
        
		// 신규 생성
		List<Map<String, Object>> domainList = new ArrayList<>();
		int lineNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			lineNumber++;

			// 헤더, 1번행 패스
			if (lineNumber < 2) {
				continue;
			}
			if (ExcelUtils.isRowEmpty(row)) {
				break;
			}
            Map<String, Object> domain = new HashMap<>();

			String id = UUID.randomUUID().toString();
            String final_version_date = ExcelUtils.getStringCellValue(row.getCell(1), false);
			if (final_version_date.isEmpty()) {
				final_version_date = LocalDate.now().toString();
			}
			String group_name = ExcelUtils.getStringCellValue(row.getCell(2), false);
			String domain_classified_name = ExcelUtils.getStringCellValue(row.getCell(3), false);
			String name = ExcelUtils.getStringCellValue(row.getCell(4), false);
			String description = ExcelUtils.getStringCellValue(row.getCell(5), false);
			String dataTypeString = ExcelUtils.getStringCellValue(row.getCell(6), false);
			if (!DataType.isValidDataTypeString(dataTypeString)) {
                log.error("invalid data_type !! - " + "(" + dataTypeString + ")");
				continue;
			}
			DataType data_type = DataType.from(dataTypeString);

			// null or "" 처리
			String dataSizeString = ExcelUtils.getStringCellValue(row.getCell(7), false);
			Long data_size = ExcelUtils.stringToLong(dataSizeString);

			// null or "" 처리
			String decimalPointSizeString = ExcelUtils.getStringCellValue(row.getCell(8));
			Long decimal_point_size = ExcelUtils.stringToLong(decimalPointSizeString);

			String save_format = ExcelUtils.getStringCellValue(row.getCell(9), true);
			String expression_format = ExcelUtils.getStringCellValue(row.getCell(10), true);
			String unit = ExcelUtils.getStringCellValue(row.getCell(11), true);
			String permission_value = ExcelUtils.getStringCellValue(row.getCell(12), true);
            String used = "Y";

            domain.put("id", id);
            domain.put("final_version_date", final_version_date);
            domain.put("group_name", group_name);
            domain.put("domain_classified_name", domain_classified_name);
            domain.put("name", name);
            domain.put("description", description);
            domain.put("data_type", data_type);
            domain.put("data_size", data_size);
            domain.put("decimal_point_size", decimal_point_size);
            domain.put("save_format", save_format);
            domain.put("expression_format", expression_format);
            domain.put("unit", unit);
            domain.put("permission_value", permission_value);
            domain.put("used", used);

            // 중복 validation
			Map<String, Object> validateResult = validateDomain(allDomainNames, domain, dataTypeMap);
			if (!(boolean) validateResult.get("validateResult")) {
				throw new Exception(validateResult.get("validateResultMessage").toString());
			}

			domainList.add(domain);
		}
		return domainList;
	}

    public Map<String, Object> validateDomain(List<String> allDomains, Map<String, Object> domain, Map<String, String> dataTypeMap) throws Exception {
        Map<String, Object> validateResultMap = new HashMap<>();
        boolean validateResult = false;
        String validateResultMessage = "";

        try {
            Set<String> allDomainNames = new HashSet<>(allDomains);
            if (!allDomainNames.add(domain.get("name").toString())) {
                validateResultMessage = "중복된 도메인명이 있습니다. - " + "(" + domain.get("name") + ")";
            } else {
                String dataTypeSign = dataTypeMap.getOrDefault(domain.get("data_type").toString(), null);
                if (dataTypeSign == null) {
                    validateResultMessage = "도메인 형식이 잘못되었습니다. - " + "(" + domain.get("name") + ")";
                } else {
                    String domainName = domain.get("name").toString();
                    String domainClassifiedName = domain.get("domain_classified_name").toString();
                    if(!domainName.startsWith(domainClassifiedName)) {
                        validateResultMessage = "도메인 형식이 잘못되었습니다. - " + "(" + domain.get("name") + ")";
                    } else if (!domainName.substring(domainClassifiedName.length()).startsWith(dataTypeSign)){
                        validateResultMessage = "도메인 형식이 잘못되었습니다. - " + "(" + domain.get("name") + ")";
                    } else {
                        validateResult = true;
                    }
                }
            }
        } catch(Exception e) {
            validateResult = false;
            validateResultMessage = "도메인 유효성 검사에 실패하였습니다.";
            log.error(validateResultMessage);
        }
        
        validateResultMap.put("validateResult", validateResult);
        validateResultMap.put("validateResultMessage", validateResultMessage);

        return validateResultMap;
    }

    public ResponseEntity<byte[]> downloadExcelFile(Map<String, Object> params) {
        try (Workbook workbook = new XSSFWorkbook()) {
            List<Map<String, Object>> list;

            String select_type = params.get("select_type") != null ? params.get("select_type").toString() : "";
            // 1. 데이터 조회
            switch (select_type) {
                case "term":
                    list = metaDataStandardMapper.getMetaDataStandardTermListForDownload(params);
                    break;
                case "word":
                    list = metaDataStandardMapper.getMetaDataStandardWordListForDownload(params);
                    break;
                case "domain":
                    list = metaDataStandardMapper.getMetaDataStandardDomainListForDownload(params);
                    break;
                default:
                    return buildErrorResponse("엑셀 다운로드에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
            }

            if (list == null || list.isEmpty()) {
                return buildErrorResponse("추출할 데이터가 없습니다.");
            }

            // 2. 엑셀 생성
            Sheet sheet = workbook.createSheet("표준 데이터");
            Map<String, Object> firstRow = new LinkedHashMap<>(list.get(0));
            Map<String, String> columnNameToKoreanMap = new LinkedHashMap<>(getKoreanHeaderMap(select_type));

            // 2-1. 헤더
            Row headerRow = sheet.createRow(0);
            int colIndex = 0;
            for (String key : firstRow.keySet()) {
                headerRow.createCell(colIndex++).setCellValue(columnNameToKoreanMap.get(key));
            }

            // 2-2. 데이터
            for (int i = 0; i < list.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                Map<String, Object> data = list.get(i);
                colIndex = 0;
                for (String key : firstRow.keySet()) {
                    Object value = data.get(key);
                    dataRow.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
                }
            }

            // 3. 엑셀 파일을 byte[]로 변환
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            byte[] fileBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename("standard-data.xlsx", StandardCharsets.UTF_8).build());

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("엑셀 다운로드 실패", e);
            return buildErrorResponse("엑셀 다운로드에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        }
    }

    // 에러 응답 생성
    private ResponseEntity<byte[]> buildErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", message);

        try {
            String json = new ObjectMapper().writeValueAsString(error);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(json.getBytes(StandardCharsets.UTF_8), headers, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(("{" + "\"errorMessage\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8),
                    new HttpHeaders(), HttpStatus.OK);
        }
    }

    private Map<String, String> getKoreanHeaderMap(String selectType) {
        Map<String, String> map = new HashMap<>();
        if (selectType.equals("term")) {
            map.put("name", "용어명");
            map.put("english_sign", "영문약어명");
            map.put("domain", "도메인명");
            map.put("final_version_date", "출처");
            map.put("created_date", "등록일자");
            map.put("used", "사용여부");
        }

        if (selectType.equals("word")) {
            map.put("name", "단어명");
            map.put("english_sign", "영문약어명");
            map.put("word_eng", "단어영문명");
            map.put("form_checked", "형식단어여부");
            map.put("domain_classified_name", "도메인분류명");
            map.put("final_version_date", "출처");
            map.put("created_date", "등록일자");
            map.put("used", "사용여부");
        }

        if (selectType.equals("domain")) {
            map.put("name", "도메인명");
            map.put("data_type", "데이터 타입");
            map.put("data_size", "데이터 길이");
            map.put("group_name", "도메인 그룹명");
            map.put("domain_classified_name", "도메인 분류명");
            map.put("final_version_date", "출처");
            map.put("created_date", "등록일자");
            map.put("used", "사용여부");
        }

        return map;
    }
}
