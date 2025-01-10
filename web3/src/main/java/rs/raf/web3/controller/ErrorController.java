package rs.raf.web3.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.web3.model.dto.ErrorDto;
import rs.raf.web3.service.ErrorService;

@RestController
@CrossOrigin
@RequestMapping("/errors")
public class ErrorController {
    private final ErrorService errorService;

    public ErrorController(ErrorService errorService) {
        this.errorService = errorService;
    }

    @GetMapping
    public ResponseEntity<Page<ErrorDto>> getErrors(
            @RequestParam int page,
            @RequestParam int size,
            @RequestHeader("Authorization") String authorization) {

        Page<ErrorDto> errors = errorService.getErrors(page, size,authorization);
        return ResponseEntity.ok(errors);
    }

    @PostMapping
    public ResponseEntity<ErrorDto> saveError(@RequestBody ErrorDto error, @RequestHeader("Authorization") String authorization) {
         errorService.saveError(error);
         return ResponseEntity.ok(error);
    }
}

