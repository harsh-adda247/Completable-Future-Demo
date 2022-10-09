package com.example.demo.controller;

import com.example.demo.entities.EmployeesEntity;
import com.example.demo.repository.EmployeesRepository;
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

    @Autowired
    private EmployeesRepository employeesRepo;

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
        logger.info("saved entities !");
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

    @GetMapping(value = "/v1/fetch/data")
    public ResponseEntity<Integer> fetchElements(@RequestParam Integer startRange,
                                                 @RequestParam Integer endRange) {
        Integer totalSize = 0;
        try {
            logger.info("fetching 1st list...");
            CompletableFuture<List<EmployeesEntity>> future1 = CompletableFuture.
                    supplyAsync(() -> employeesRepo.findAll());
            logger.info("fetching 2nd list..");
            CompletableFuture<List<EmployeesEntity>> future2 = CompletableFuture.
                    supplyAsync(() -> employeesRepo.findByIdRange(startRange, endRange));
            logger.info("haven't blocked yet...");
            List<EmployeesEntity> list1 = future1.get();
            List<EmployeesEntity> list2 = future2.get();
            logger.info("fetched both of the lists...");
            totalSize = totalSize.intValue() + list1.size() + list2.size();
        } catch (Exception e) {
            logger.error("Error occurred while future...", e);
        }
        System.out.println();
        return new ResponseEntity<>(totalSize, HttpStatus.OK);
    }

    @GetMapping(value = "/v2/fetch/data")
    public ResponseEntity<Integer> fetchElementsV2(@RequestParam Integer startRange,
                                                   @RequestParam Integer endRange) {
        Integer totalSize = 0;
        logger.info("fetching 1st list...");
        List<EmployeesEntity> list1 = employeesRepo.findAll();
        logger.info("fetching 2nd list..");
        List<EmployeesEntity> list2 = employeesRepo.findByIdRange(startRange, endRange);
        logger.info("fetched both of the lists...");
        totalSize = totalSize.intValue() + list1.size() + list2.size();
        System.out.println();
        
        return new ResponseEntity<>(totalSize, HttpStatus.OK);
    }
}
