package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Property;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Integer> {
    List<Property> findByCityId(int cityId);

    Property findBySlug(String slug);

    @Query(value = "SELECT * FROM properties p " +
            "WHERE unaccent(lower(p.name)) ILIKE unaccent(:keyword) AND rating_star ILIKE rateStar AND p.property_type ILIKE :propertyType " +
            "AND( unaccent(lower(p.property_type)) ILIKE unaccent(:keyword) " +
            "OR EXISTS(SELECT 1 FROM properties p1 join cities c on p1.city_id=c.id WHERE unaccent(c.name) ILIKE unaccent(:keyword)))",
            countQuery = "SELECT count(*) FROM properties p " +
                    "WHERE unaccent(lower(p.name)) ILIKE unaccent(:keyword) AND rating_star=:rateStar AND p.property_type ILIKE :propertyType " +
                    "AND (unaccent(lower(p.property_type)) ILIKE unaccent(:keyword) "+
                    "OR EXISTS(SELECT 1 FROM cities c  WHERE p.city_id=c.id AND unaccent(c.name) ILIKE unaccent(:keyword))",
            nativeQuery = true)
    Page<Property> findByKeyword(@Param("keyword") String keyword,
                                 @Param("rateStar") int rateStar,
                                 @Param("propertyType") String propertyType,
                                 Pageable pageable);

    Property findByName(@NotBlank(message = "name not blank") String name);
}