package com.tdtu.interaction_services.dtos.respone;

import com.tdtu.interaction_services.enums.EReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactResponseNumOfReaction {
    Map<EReactionType, List<ReactResponse>> reactions = new HashMap<>();
    private int countReactions;
    private boolean hasReacted;

}
