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
    @Operation(summary = "List all engineers", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content)
    })
    public ResponseEntity<List<Engineer>> listAll(){
        return new ResponseEntity<>(engineerService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find an engineer by id", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Engineer not found", content = @Content),
    })
    public ResponseEntity<Engineer> findById(@PathVariable long id){
        return ResponseEntity.ok(engineerService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find") // /engineers/find?name=engineername
    @Operation(summary = "Find an engineer by name", description = "/houses/find?name=Engineer+name+here", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content)
    })
    public ResponseEntity <List<Engineer>> findByName(@RequestParam(required = false) String name){
        return ResponseEntity.ok(engineerService.findByName(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new engineer", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "201", description = "Successful operation, created", content = @Content),
            @ApiResponse(responseCode = "500", description = "The engineer must have a name", content = @Content)
    })
    public ResponseEntity<Engineer> save(@RequestBody EngineerPostRequestBody engineerPostRequestBody){
        return new ResponseEntity<>(engineerService.save(engineerPostRequestBody), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete an engineer by id", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "204", description = "Successful operation, deleted"),
            @ApiResponse(responseCode = "400", description = "Engineer not found"),
            @ApiResponse(responseCode = "500", description = "You cannot delete an engineer that is linked to a house, " +
                    "need to get the engineer out of this house via the house endpoints")
    })
    public ResponseEntity<Void> delete(@PathVariable long id){
        engineerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @Operation(summary = "Updates an engineer", description = "You must pass the name and the id, even if you want what already is", security = { @SecurityRequirement(name = "bearer-key") } )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized, without any authentication"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "204", description = "Successful operation, updated"),
            @ApiResponse(responseCode = "400", description = "Engineer not found"),
            @ApiResponse(responseCode = "500", description = "The engineer must have a name, or you dont or you didn't pass id field")
    })
    public ResponseEntity<Void> replace(@RequestBody EngineerPutRequestBody engineerPutRequestBody){
        engineerService.replace(engineerPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
