package com.sw300.community.category.service;

import com.sw300.community.category.dto.CategoryDto;
import com.sw300.community.category.model.Category;
import com.sw300.community.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategory() {
        List<Category> result = categoryRepository.findAll();

        List<CategoryDto> dtoList = result.stream()
                .map(category -> modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());

        return dtoList;
    }

    public List<CategoryDto> getBestCategory() {

        Pageable pageable = PageRequest.of(0, 3); // 상위 3개를 가져오기 위한 페이지 설정

        List<Category> result = categoryRepository.findByDailyVisitors(pageable);

        List<CategoryDto> dtoList = result.stream()
                .map(category -> modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());

        return dtoList;
    }

    public static final String LAST_VISIT_TIME_SESSION_KEY = "lastVisitTime";
    public static final long VISIT_INTERVAL_MS = TimeUnit.HOURS.toMillis(1);

    @Transactional
    public void incrementDailyVisitors(Long cno, HttpSession session) {

        // 카테고리별 세션 키 생성
        String categorySessionKey = LAST_VISIT_TIME_SESSION_KEY + "_" + cno;

        // 카테고리별 세션에서 이전 방문 시간을 가져옴
        Long lastVisitTime = (Long) session.getAttribute(categorySessionKey);
        long currentTimeMillis = System.currentTimeMillis();

        if (lastVisitTime == null || currentTimeMillis - lastVisitTime >= VISIT_INTERVAL_MS) {
            // 방문 횟수 초기화 및 방문 시간 업데이트
            session.setAttribute(categorySessionKey, currentTimeMillis);

            Category category = categoryRepository.findById(cno).orElse(null);

            if (category != null) {
                // 일별 방문자 수를 1 증가시킴
                int currentVisitors = category.getDailyVisitors();
                category.changeDailyVisiters(currentVisitors + 1);

                // 엔티티를 저장하여 방문자 수를 업데이트
                categoryRepository.save(category);
            }
        }
    }

    // 일정 주기로 실행할 스케줄링 메서드
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void resetDailyVisitors() {
        // 현재 시간을 체크하여 자정에만 실행되도록 함
        LocalTime currentTime = LocalTime.now();
        if (currentTime.getHour() == 0 && currentTime.getMinute() == 0) {
            // 카테고리 테이블의 dailyVisitors 값을 모두 0으로 초기화
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
            } else {
                for (Category category : categories) {
                    category.changeDailyVisiters(0);
                }
                categoryRepository.saveAll(categories);
            }
        }
    }
}
