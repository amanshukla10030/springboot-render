package com.happyghumakkads.repository;

import com.happyghumakkads.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    
    List<Package> findByStatus(Package.PackageStatus status);
    
    List<Package> findByType(Package.PackageType type);
    
    List<Package> findByDestinationContainingIgnoreCase(String destination);
    
    @Query("SELECT p FROM Package p WHERE p.status = 'ACTIVE' ORDER BY p.createdAt DESC")
    List<Package> findActivePackages();
    
    @Query("SELECT p FROM Package p WHERE p.status = 'ACTIVE' AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Package> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Package p WHERE p.status = 'ACTIVE' AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.destination) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Package> searchPackages(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM Package p WHERE p.status = 'ACTIVE' ORDER BY p.price ASC")
    List<Package> findActivePackagesByPriceAsc();
    
    @Query("SELECT p FROM Package p WHERE p.status = 'ACTIVE' ORDER BY p.price DESC")
    List<Package> findActivePackagesByPriceDesc();
}
