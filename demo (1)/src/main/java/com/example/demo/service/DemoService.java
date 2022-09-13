package com.example.demo.service;

import com.example.demo.entities.EmployeesEntity;
import com.example.demo.repository.EmployeesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DemoService {

    private Logger logger = LoggerFactory.getLogger(DemoService.class);

    @Autowired
    private EmployeesRepository employeesRepo;


    /**
     * method to save data inside DB parallelly, in the background while response
     * was already returned to the user
     */
    public void saveEntities() {
        List<EmployeesEntity> entities = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            EmployeesEntity entity = new EmployeesEntity(i, "Name" + i);
            entities.add(entity);
        }
        employeesRepo.saveAll(entities);
        logger.info("Saved all entities");
    }

    /**
     * method to get all 10K entities instances
     *
     * @return
     */
    public List<EmployeesEntity> getAllEmployees() {
        logger.info("listing process initiated....");
        List<EmployeesEntity> entities = new ArrayList<>();
        entities = employeesRepo.findAll();
        if (CollectionUtils.isEmpty(entities)) entities = new ArrayList<>();
        logger.info("list size : " + entities.size());
        return entities;
    }

    /**
     * method to search a provided element into the array of 10K elements
     *
     * @param array
     * @param element
     */
    public void searchAndPrint(int[] array, int element) {
        logger.info("searching process initiated....");
        int index = search(array, element);
        if (index == -1) logger.info("Element not found !");
        else logger.info("Printing " + element + " into console !");
    }

    /**
     * method to search an element into an array of 10K elements using binary search
     *
     * @param arr
     * @param elementToBeSearched
     * @return
     */
    private int search(int[] arr, int elementToBeSearched) {
        int beg = 0, end = arr.length - 1, mid = (beg + end) / 2;
        //Base Case
        if (end <= 0) return -1;
        while (beg <= end && arr[mid] != elementToBeSearched) {
            if (arr[mid] < elementToBeSearched) {
                beg = mid + 1;
            } else if (arr[mid] > elementToBeSearched) {
                end = mid - 1;
            }
            mid = (beg + end) / 2;
        }
        if (arr[mid] == elementToBeSearched) return mid;
        return -1;
    }
}
