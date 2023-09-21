package project.house.builders.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation(summary = "List all house projects", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<List<House>> listAll(){
        return new ResponseEntity<>(houseService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find a house by id", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "400", description = "House project not found", content = @Content),
    })
    public ResponseEntity<House> findById(@PathVariable long id){
        return ResponseEntity.ok(houseService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    @Operation(summary = "Find a house by name", description = "/houses/find?name=Houseproject+name+here", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content)
    })
    public ResponseEntity <List<House>> findByName(@RequestParam(required = false) String name){
        return ResponseEntity.ok(houseService.findByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new house", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "201", description = "Successful operation, created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Engineer or Architect not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "The house project must have a name or a nickname", content = @Content)
    })
    public ResponseEntity<House> save(@RequestBody HousePostRequestBody housePostRequestBody){
        return new ResponseEntity<>(houseService.save(housePostRequestBody), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a house by id", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "204", description = "Successful operation, deleted"),
            @ApiResponse(responseCode = "400", description = "House project not found")
    })
    public ResponseEntity<Void> delete(@PathVariable long id){
        houseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @Operation(summary = "Updates a house", description = "You must pass the name and the id, if you leave architect and engineer blank they update to null"
            , security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "204", description = "Successful operation, updated"),
            @ApiResponse(responseCode = "400", description = "Engineer, Architect or House not found"),
            @ApiResponse(responseCode = "500", description = "The house project must have a name or a nickname, or you dont or you didn't pass id field")
    })
    public ResponseEntity<Void> replace(@RequestBody HousePutRequestBody housePutRequestBody){
        houseService.replace(housePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
