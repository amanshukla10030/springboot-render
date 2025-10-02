package com.happyghumakkads.controller;

import com.happyghumakkads.entity.Package;
import com.happyghumakkads.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public String listPackages(@RequestParam(value = "q", required = false) String q,
                               @RequestParam(value = "destination", required = false) String destination,
                               Model model) {
        List<Package> packages;
        if (q != null && !q.isBlank()) {
            packages = packageService.searchPackages(q);
        } else if (destination != null && !destination.isBlank()) {
            packages = packageService.getPackagesByDestination(destination);
        } else {
            packages = packageService.getActivePackages();
        }
        model.addAttribute("packages", packages);
        return "packages/list";
    }

    @GetMapping("/{id}")
    public String packageDetail(@PathVariable Long id, Model model) {
        Package pkg = packageService.findById(id).orElse(null);
        if (pkg == null) {
            return "redirect:/packages";
        }
        model.addAttribute("pkg", pkg);
        return "packages/detail";
    }
}
