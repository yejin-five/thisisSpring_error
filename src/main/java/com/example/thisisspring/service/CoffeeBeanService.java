package com.example.thisisspring.service;

import com.example.thisisspring.domain.CoffeeBean;
import com.example.thisisspring.dto.CoffeeBeanDto;
import com.example.thisisspring.repository.CoffeeBeanRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoffeeBeanService {

    private final CoffeeBeanRepository coffeeBeanRepository;


    public CoffeeBeanService(CoffeeBeanRepository coffeeBeanRepository) {
        this.coffeeBeanRepository = coffeeBeanRepository;

    }

    @PostConstruct
    public void saveTenCoffeeBeansEfficient() {
        List<CoffeeBean> coffeeBeans = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            String coffeeName = "커피 이름" + i;
            int quantity = 100;
            CoffeeBean coffeeBean = new CoffeeBean(coffeeName, quantity);
            coffeeBeans.add(coffeeBean);
        }

        coffeeBeanRepository.saveAll(coffeeBeans);
    }

    public List<CoffeeBeanDto> getAllCoffeeBeansDto() {
        List<CoffeeBean> coffeeBeans = coffeeBeanRepository.findAll();

        return coffeeBeans.stream()
                .map(coffeeBean -> new CoffeeBeanDto(coffeeBean.getId(), coffeeBean.getName(), coffeeBean.getQuantity()))
                .collect(Collectors.toList());

    }

    public void updateCoffeeBeanQuantity(String name, int quantityToAdd) {
        CoffeeBean coffeeBean = coffeeBeanRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("해당 이름의 커피 데이터를 찾을 수 없습니다."));

        coffeeBean.setQuantity(coffeeBean.getQuantity() + quantityToAdd);
        coffeeBeanRepository.save(coffeeBean);

    }
}