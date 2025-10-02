package com.happyghumakkads.service;

import com.happyghumakkads.entity.Package;
import com.happyghumakkads.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {
    
    @Autowired
    private PackageRepository packageRepository;
    
    public Package savePackage(Package packageEntity) {
        return packageRepository.save(packageEntity);
    }
    
    public Optional<Package> findById(Long id) {
        return packageRepository.findById(id);
    }
    
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }
    
    public List<Package> getActivePackages() {
        return packageRepository.findActivePackages();
    }
    
    public List<Package> getPackagesByType(Package.PackageType type) {
        return packageRepository.findByType(type);
    }
    
    public List<Package> searchPackages(String keyword) {
        return packageRepository.searchPackages(keyword);
    }
    
    public List<Package> getPackagesByDestination(String destination) {
        return packageRepository.findByDestinationContainingIgnoreCase(destination);
    }
    
    public List<Package> getPackagesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return packageRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    public List<Package> getPackagesSortedByPrice(boolean ascending) {
        if (ascending) {
            return packageRepository.findActivePackagesByPriceAsc();
        } else {
            return packageRepository.findActivePackagesByPriceDesc();
        }
    }
    
    public Package updatePackage(Package packageEntity) {
        return packageRepository.save(packageEntity);
    }
    
    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }
    
    public boolean isPackageAvailable(Long packageId) {
        Optional<Package> packageOpt = findById(packageId);
        return packageOpt.isPresent() && packageOpt.get().getStatus() == Package.PackageStatus.ACTIVE;
    }
}
