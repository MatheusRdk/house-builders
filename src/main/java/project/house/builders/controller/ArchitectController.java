package project.house.builders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "List all achitects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content)
    })
    public ResponseEntity<List<Architect>> listAll(){
        return new ResponseEntity<>(architectService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find an architect by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content),
            @ApiResponse(responseCode = "400", description = "Architect not found", content = @Content),
    })
    public ResponseEntity<Architect> findById(@PathVariable long id){
        return ResponseEntity.ok(architectService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find") // /architects/find?name=architectname
    @Operation(summary = "Find an architect by name", description = "/houses/find?name=Architect+name+here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content)
    })
    public ResponseEntity <List<Architect>> findByName(@RequestParam(required = false) String name){
        return ResponseEntity.ok(architectService.findByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new architect")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "201", description = "Successful operation, created", content = @Content),
            @ApiResponse(responseCode = "500", description = "The architect must have a name", content = @Content)
    })
    public ResponseEntity<Architect> save(@RequestBody ArchitectPostRequestBody architectPostRequestBody){
        return new ResponseEntity<>(architectService.save(architectPostRequestBody), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete an architect by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "204", description = "Successful operation, deleted"),
            @ApiResponse(responseCode = "400", description = "Architect not found"),
            @ApiResponse(responseCode = "500", description = "You cannot delete an architect that is linked to a house, " +
                    "need to get the architect out of this house via the house endpoints")
    })
    public ResponseEntity<Void> delete(@PathVariable long id){
        architectService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @Operation(summary = "Updates an architect", description = "You must pass the name and the id, even if you want what already is")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "204", description = "Successful operation, updated"),
            @ApiResponse(responseCode = "400", description = "Architect not found"),
            @ApiResponse(responseCode = "500", description = "The architect must have a name, or you dont or you didn't pass id field")
    })
    public ResponseEntity<Void> replace(@RequestBody ArchitectPutRequestBody architectPutRequestBody){
        architectService.replace(architectPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}