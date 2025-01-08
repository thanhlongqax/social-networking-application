package com.tdtu.newsfeed_service.dtos.respone;



import com.tdtu.newsfeed_service.enums.EReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopReacts implements Serializable {
    private EReactionType type;
    private int count;
}