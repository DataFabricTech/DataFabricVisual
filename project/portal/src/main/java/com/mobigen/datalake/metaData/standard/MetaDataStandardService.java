package com.mobigen.datalake.metaData.standard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            term.put("used", name);
            term.put("final_version_date", name);
            term.put("creator", creator);
            term.put("modifier", modifier);

			// 중복 validation
			boolean validateResult = validate(allTerms, allDomainNames, term);
			if (!validateResult) {
				throw new Exception("vlidate Error !!");
			}

			termList.add(term);
		}
		return termList;
	}

    public boolean validate(List<Map<String, Object>> allTerms, List<String> allDomainNames, Map<String, Object> term) {
        boolean nameDuplicated = false;
        boolean englishSignDuplicated = false;
        boolean domainNameDoesntExist = true;

        Set<String> allTermNames = new HashSet<>();
        Set<String> allTermEnglishSigns = new HashSet<>();;
        Set<String> domainNames = new HashSet<>(allDomainNames);
        
        for (Map<String, Object> item : allTerms) {
            allTermNames.add(item.get("name").toString());
            allTermEnglishSigns.add(item.get("english_sign").toString());
        }

        if (!allTermNames.add(term.get("name").toString())) {
            nameDuplicated = true;
        }
        if (!allTermEnglishSigns.add(term.get("english_sign").toString())) {
            englishSignDuplicated = true;
        }
        if (domainNames.contains(term.get("domain").toString())) {
            domainNameDoesntExist = false;
        }

        if (!nameDuplicated && !englishSignDuplicated && !domainNameDoesntExist) {
            return true;
        }

        if (nameDuplicated) {
            log.error("Name Duplicated !! - " + "(" + term.get("name") + ")");
        }
        if (englishSignDuplicated) {
            log.error("English Sign Duplicated !! - " + "(" + term.get("english_sign") + ")");
        }
        if (domainNameDoesntExist) {
            log.error("domain Duplicated !! - " + "(" + term.get("domain") + ")");
        }
        return false;
    }
}
