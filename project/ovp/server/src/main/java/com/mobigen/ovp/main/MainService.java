package com.mobigen.ovp.main;

import com.mobigen.ovp.model_creation.ModelCreationService;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final ModelCreationService modelCreationService;

    /**
     * 메인 >
     *
     * @return
     * @throws Exception
     */
    public Object getUserFollows(String id) {
        return modelCreationService.getMyModelList(id, "", "follows", 3);
    }
}
