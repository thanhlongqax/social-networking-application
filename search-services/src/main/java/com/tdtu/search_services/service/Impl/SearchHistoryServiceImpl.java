package com.tdtu.search_services.service.Impl;

import com.tdtu.search_services.dto.ResDTO;
import com.tdtu.search_services.model.SearchHistory;
import com.tdtu.search_services.repository.SearchHistoryRepository;
import com.tdtu.search_services.service.SearchHistoryService;
import com.tdtu.search_services.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    private final JwtUtils jwtUtils;
    // Lưu lịch sử tìm kiếm
    public ResDTO<?> saveSearchHistory(String token, String keyword) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        if (userId == null || userId.isEmpty()) {
            return new ResDTO<>(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ", null);
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            return new ResDTO<>(HttpServletResponse.SC_BAD_REQUEST, "Từ khóa tìm kiếm không được để trống", null);
        }

        try {
            SearchHistory history = new SearchHistory();
            history.setUserId(userId);
            history.setKeyword(keyword);
            history.setCreatedAt(LocalDateTime.now());
            searchHistoryRepository.save(history);

            return new ResDTO<>(HttpServletResponse.SC_OK, "Lưu lịch sử thành công", null);
        } catch (Exception e) {
            return new ResDTO<>(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lưu lịch sử tìm kiếm " + e.getMessage(), null);
        }
    }

    // Lấy danh sách lịch sử tìm kiếm của một người dùng
    public ResDTO<List<SearchHistory>> getUserSearchHistory(String token) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        if (userId == null || userId.isEmpty()) {
            return new ResDTO<>(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ", null);
        }

        List<SearchHistory> histories = searchHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return new ResDTO<>(HttpServletResponse.SC_OK, "Lấy danh sách lịch sử tìm kiếm thành công", histories);
    }

    // Xóa một lịch sử tìm kiếm theo ID
    public ResDTO<?> deleteSearchHistoryById(String id) {
        if (!searchHistoryRepository.existsById(id)) {
            return new ResDTO<>(HttpServletResponse.SC_NOT_FOUND, "Lịch sử tìm kiếm không tồn tại", null);
        }

        searchHistoryRepository.deleteById(id);
        return new ResDTO<>(HttpServletResponse.SC_OK, "Xóa lịch sử tìm kiếm thành công", null);
    }

    // Xóa toàn bộ lịch sử tìm kiếm của một người dùng
    public ResDTO<?> deleteAllSearchHistory(String token) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        if (userId == null || userId.isEmpty()) {
            return new ResDTO<>(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ", null);
        }

        List<SearchHistory> histories = searchHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);

        if (histories.isEmpty()) {
            return new ResDTO<>(HttpServletResponse.SC_NOT_FOUND, "Không có lịch sử tìm kiếm để xóa", null);
        }

        searchHistoryRepository.deleteAll(histories);
        return new ResDTO<>(HttpServletResponse.SC_OK, "Đã xóa toàn bộ lịch sử tìm kiếm", null);
    }

}

