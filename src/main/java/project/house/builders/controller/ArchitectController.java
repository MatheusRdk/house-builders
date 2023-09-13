package project.house.builders.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.house.builders.domain.Architect;
import project.house.builders.requests.ArchitectPostRequestBody;
import project.house.builders.requests.ArchitectPutRequestBody;
import project.house.builders.service.ArchitectService;

import java.util.List;

@RestController
@RequestMapping("architects")
@Log4j2
@RequiredArgsConstructor
public class ArchitectController {

    private final ArchitectService architectService;


    @GetMapping(path = "/all")
    public ResponseEntity<List<Architect>> listAll(){
        return new ResponseEntity<>(architectService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Architect> findById(@PathVariable long id){
        return ResponseEntity.ok(architectService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find") // /architects/find?name=architectname
    public ResponseEntity <List<Architect>> findByName(@RequestParam(required = false) String name){
        return ResponseEntity.ok(architectService.findByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Architect> save(@RequestBody ArchitectPostRequestBody architectPostRequestBody){
        return new ResponseEntity<>(architectService.save(architectPostRequestBody), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        architectService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody ArchitectPutRequestBody architectPutRequestBody){
        architectService.replace(architectPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}