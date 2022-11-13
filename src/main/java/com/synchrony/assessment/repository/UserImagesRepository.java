package com.synchrony.assessment.repository;

import com.synchrony.assessment.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImagesRepository extends JpaRepository<UserImage, Long> {
}
