package com.synchrony.assessment.controller;

import com.synchrony.assessment.exception.UserCustomException;
import com.synchrony.assessment.model.User;
import com.synchrony.assessment.service.UserService;
import com.synchrony.assessment.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description: This is REST Controller or Resource class defines all end points
 *               1. User Registration with user basic information.
 *               2. Associate the list of images uploaded by
 *               user in UserImages table in H2 DB. 3. Upload, View and Delete
 *               image to Imgur as well as in H2 database having One-To-Many
 *               relationship.
 *
 * @author Yasmeen
 *
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;

    /**
     * User registration end point and stores in H2 DB
     *
     * @return ResponseEntity Ack message.
     */
    @PostMapping(value = "/synchrony/registration")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        boolean success = userService.userRegister(user);
        if(!success)
            throw new UserCustomException("User has already Registered!");
        log.info("The Successfully Registered user is " + user.getUserName());
        return new ResponseEntity<>("User Registered successfully", HttpStatus.CREATED);
    }

    /**
     * End point to retrieve a user information for specific User.
     *
     * @param userId
     * @return User
     */
    @GetMapping(value = "/synchrony/users/{userId}")
    public User retrieveUser(@PathVariable("userId") Long userId) {
        log.info("The requested UserId is " + userId);
        return userService.retrieveUser(userId);
    }

    /**
     * End point to delete uploaded image on IMGUR.
     *
     * @param user
     * @param deletehash
     * @return response as acknowledgment
     */
    @DeleteMapping(value = "/synchrony/delete/{deletehash}")
    public ResponseEntity<Object> deleteImage(@RequestBody User user, @PathVariable("deletehash") String deletehash) {
        if (userService.isUserExistsWithDeletePermission(user)) {
            userService.deleteImage(user, deletehash);
            log.info("The image is deleted with hash " + deletehash);
            return new ResponseEntity<>("Image Deleted successfully", HttpStatus.CREATED);
        } else {
            log.info("The image is not deleted with hash " + deletehash);
            throw new UserCustomException("User is not authorizes to delete Image from Imgur!");
        }
    }

    /**
     * End point to view User and their uploaded images details.
     *
     * @param userId
     * @return User
     */
    @GetMapping(value = "/synchrony/viewuserimages/{userId}")
    public User viewUserAndImagesDetails(@PathVariable("userId") Long userId) {
        return userService.viewUserAndImagesDetails(userId);
    }

    /**
     * End point to retrieve all Users information from DB
     *
     * @return List of Users
     */
    @GetMapping(value = "/synchrony/users")
    public List<User> retrieveUsers() {
        List<User> users = userService.retrieveUsers();
        return users;
    }

    /**
     * End point to upload image to IMGUR portal.
     *
     * @param user
     * @RequestParam file
     * @return response as Acknowledgement.
     */
    @PostMapping(value = "/synchrony/upload")
    public ResponseEntity<Object> uploadImage(@RequestBody User user, @RequestParam("file") MultipartFile file) {
        if (userService.isUserExistsWithUploadPermission(user)) {
            String response = userService.uploadImage(file);
            userService.persistUserImages(file);
            log.info("The image is uploaded successfully");
            return new ResponseEntity<>("Upload Image successfully \n" + response, HttpStatus.CREATED);
        } else {
            log.info("The image is not uploaded successfully");
            throw new UserCustomException("User is not authorizes to upload Image to Imgur!");
        }
    }

}
