package com.example.demo.controller;

import com.example.demo.entities.EmployeesEntity;
import com.example.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class DemoController {

    private int[] array = new int[1000000];

    @Autowired
    private DemoService demoService;

    private Logger logger = LoggerFactory.getLogger("DemoController");

    public DemoController() {
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }
    }

    @GetMapping("/save/entities/without/parallel")
    @ResponseBody
    public ResponseEntity<?> saveEntities() {
        logger.info("saving entities initiated...");
        demoService.saveEntities();
        logger.info("returning response while saving....");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/save/entities")
    @ResponseBody
    public ResponseEntity<?> saveEntitiesInParallel() {
        try {
            CompletableFuture.runAsync(() -> demoService.saveEntities());
        } catch (Exception e) {
            logger.error("Exception occurred while saving : " + e.getMessage());
        }
        logger.info("returning response while saving....");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/async/multiple")
    @ResponseBody
    public ResponseEntity<?> asyncProcess(@RequestParam(name = "element")
                                          Integer elementToBeSearched) {
        System.out.println();
        try {
            CompletableFuture<List<EmployeesEntity>> data = CompletableFuture.supplyAsync(() -> {
                return demoService.getAllEmployees();
            });
            CompletableFuture.runAsync(() ->
                    demoService.searchAndPrint(array, elementToBeSearched));
            List<EmployeesEntity> list = data.get();
            logger.info("list size in controller : " + list.size());
        } catch (Exception e) {
            logger.error("Exception occurred while completable future execution : " + e.getMessage());
        }
        logger.info("returning response....");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
