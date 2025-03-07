package com.tdtu.search_services.repository;

import com.tdtu.search_services.model.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, String> {
    List<UserDocument> findByUsernameContainingOrFirstNameContainingOrMiddleNameContainingOrLastNameContaining(
            String username, String firstName, String middleName, String lastName);
}

