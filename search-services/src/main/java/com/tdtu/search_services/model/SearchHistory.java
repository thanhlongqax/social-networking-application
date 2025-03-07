package com.tdtu.search_services.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tdtu.search_services.enums.EUserRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "search_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId; // ID người dùng


    private String keyword; // Từ khóa tìm kiếm

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt; //thời gian tìm kiem
}

