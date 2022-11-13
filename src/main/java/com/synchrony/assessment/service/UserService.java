package com.synchrony.assessment.service;

import com.synchrony.assessment.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    static final String IMGUR_BASE_IMAGE_URL = "https://api.imgur.com/3/image";

    //imgur clientId
    static final String IMGUR_CLIENTID = "d2f039fc398c7c3";

    boolean userRegister(User user);

    List<User> retrieveUsers();

    void persistUserImages(MultipartFile image);

    User retrieveUser(Long id);

    boolean isUserExistsWithUploadPermission(User user);

    boolean isUserExistsWithViewPermission(User user);

    boolean isUserExistsWithDeletePermission(User user);


    User viewUserAndImagesDetails(Long id);

    String uploadImage(MultipartFile image);

    String deleteImage(User user, String deleteHash);
}

