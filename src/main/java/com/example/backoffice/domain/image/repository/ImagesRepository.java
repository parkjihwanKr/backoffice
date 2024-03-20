package com.example.backoffice.domain.image.repository;

import com.example.backoffice.domain.image.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images, Long> {

}
