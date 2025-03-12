package com.mindfire.backend.controller;

import com.mindfire.backend.dto.request.UserRequestDto;
import com.mindfire.backend.dto.response.PageResponse;
import com.mindfire.backend.dto.response.UserResponseDto;
import com.mindfire.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-service")
public class UserController {
    private final UserService userService;

    @PostMapping("/user-data")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = userService.create(userRequestDto);
        return new ResponseEntity<UserResponseDto>(response, HttpStatus.CREATED);
    }

    @PutMapping("user/user-data/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto response = userService.update(id, userRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/user-data/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        boolean isDeleted = userService.delete(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("user/user-data/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable long id) {
        UserResponseDto response = userService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/user-data")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> response = userService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user-data/paginated")
    public ResponseEntity<PageResponse<UserResponseDto>> getAllUsersPaginated(@RequestParam int pageNumber ,@RequestParam int pageSize) {
    return  new ResponseEntity<>(userService.getPaginatedUser(pageNumber,pageSize),HttpStatus.OK);
    }

}
