package com.example.thisisspring.controller;

import com.example.thisisspring.domain.CoffeeBean;
import com.example.thisisspring.dto.CoffeeBeanDto;
import com.example.thisisspring.repository.CoffeeBeanRepository;
import com.example.thisisspring.service.CoffeeBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/coffee")
public class CoffeeBeanController {
    private final CoffeeBeanService coffeeBeanService;
    private final CoffeeBeanRepository coffeeBeanRepository;

    @Autowired
    public CoffeeBeanController(CoffeeBeanService coffeeBeanService, CoffeeBeanRepository coffeeBeanRepository){
        this.coffeeBeanService = coffeeBeanService;
        this.coffeeBeanRepository = coffeeBeanRepository;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCoffeeBeanById(@PathVariable Long id) {
        if (coffeeBeanRepository.existsById(id)) { // 해당 id 값의 데이터가 있는 경우 처리
            coffeeBeanRepository.deleteById(id);
            return new ResponseEntity<>("커피 데이터가 삭제되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 ID의 커피 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public String createCoffeeBeans() {
        coffeeBeanService.saveTenCoffeeBeansEfficient();
        return "10개의 카페 데이터가 생성되었습니다.";
    }

    @GetMapping("/list")
    public ResponseEntity<List<CoffeeBeanDto>>getAllCoffeeBeans(){
        List<CoffeeBeanDto> coffeeBeansDto = coffeeBeanService.getAllCoffeeBeansDto();

        if(coffeeBeansDto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(coffeeBeansDto,HttpStatus.OK);

        }
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateCoffeeBeanQuantity(@RequestBody CoffeeBeanDto coffeeBeanDto) {
        try {
            coffeeBeanService.updateCoffeeBeanQuantity(coffeeBeanDto.getName(), coffeeBeanDto.getQuantity());
            return ResponseEntity.ok("커피 데이터의 재고가 업데이트 되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("커피 데이터 재고 업데이트 중에 오류가 발생했습니다.");
        }
    }
}