package project.house.builders.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.house.builders.domain.House;
import project.house.builders.requests.HousePostRequestBody;
import project.house.builders.requests.HousePutRequestBody;
import project.house.builders.service.HouseService;

import java.util.List;

@RestController
@RequestMapping("houses")
@Log4j2
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;


    @GetMapping(path = "/all")
    public ResponseEntity<List<House>> listAll(){
        return new ResponseEntity<>(houseService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<House> findById(@PathVariable long id){
        return ResponseEntity.ok(houseService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find") // /houses/find?name=Houseprojectname
    public ResponseEntity <List<House>> findByName(@RequestParam(required = false) String name){
        return ResponseEntity.ok(houseService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<House> save(@RequestBody HousePostRequestBody housePostRequestBody){
        return new ResponseEntity<>(houseService.save(housePostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        houseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody HousePutRequestBody housePutRequestBody){
        houseService.replace(housePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
