package project.house.builders.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.house.builders.domain.Engineer;
import project.house.builders.requests.EngineerPostRequestBody;
import project.house.builders.requests.EngineerPutRequestBody;
import project.house.builders.service.EngineerService;

import java.util.List;

@RestController
@RequestMapping("engineers")
@Log4j2
@RequiredArgsConstructor
public class EngineerController {

    private final EngineerService engineerService;


    @GetMapping(path = "/all")
    public ResponseEntity<List<Engineer>> listAll(){
        return new ResponseEntity<>(engineerService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Engineer> findById(@PathVariable long id){
        return ResponseEntity.ok(engineerService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find") // /engineers/find?name=engineername
    public ResponseEntity <List<Engineer>> findByName(@RequestParam(required = false) String name){
        return ResponseEntity.ok(engineerService.findByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Engineer> save(@RequestBody EngineerPostRequestBody engineerPostRequestBody){
        return new ResponseEntity<>(engineerService.save(engineerPostRequestBody), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        engineerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody EngineerPutRequestBody engineerPutRequestBody){
        engineerService.replace(engineerPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
