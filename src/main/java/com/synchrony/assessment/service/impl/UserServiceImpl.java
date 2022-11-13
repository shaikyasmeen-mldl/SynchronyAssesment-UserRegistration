package com.synchrony.assessment.service.impl;

import com.synchrony.assessment.model.User;
import com.synchrony.assessment.model.UserImage;
import com.synchrony.assessment.repository.UserImagesRepository;
import com.synchrony.assessment.repository.UserRepository;
import com.synchrony.assessment.service.UserService;
import com.synchrony.assessment.util.UserUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private Lock lock;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserImagesRepository userImagesRepository;

    @Autowired
    private UserUtility userUtil;

    @Override
    public boolean userRegister(User user) {
        lock = new ReentrantLock();
        try {
            lock.lock();
            ExampleMatcher nameMatcher = ExampleMatcher.matching().withMatcher("username",
                    ExampleMatcher.GenericPropertyMatchers.caseSensitive());
            Example<User> userExample = Example.of(user, nameMatcher);
            boolean exists = userRepository.exists(userExample);
            if (!exists) {
                String password = userUtil.decryptPassword(user);
                if (!password.isEmpty())
                    user.setPassword(password);
                userRepository.save(user);
                log.info("The User is saved successfully " + user.getUserName());
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public List<User> retrieveUsers() {
        List<User> users = userRepository.findAll();
        log.info("The retrieved users are " + users);
        return users;
    }

    @Override
    public void persistUserImages(MultipartFile image) {
        UserImage images = new UserImage();
        lock = new ReentrantLock();
        try {
            lock.lock();
            images.setImage(image.getBytes());
            userImagesRepository.save(images);
            log.info("The images are saved successfully - " + images);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    @Override
    public boolean isUserExistsWithUploadPermission(User user) {
        String uploadPerms = "";
        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("username",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive());
        Example<User> userExample = Example.of(user, NAME_MATCHER);
        boolean exists = userRepository.exists(userExample);
        if (exists)
            uploadPerms = user.getPermission();
        return (!uploadPerms.isEmpty() && uploadPerms.equals("Upload") ? true : false);
    }

    @Override
    public User retrieveUser(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        log.info("The retrieved users is of id " + id);
        return optUser.get();
    }

    @Override
    public User viewUserAndImagesDetails(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        log.info("The User and image details are " + optUser);
        return optUser.get();
    }

    @Override
    public boolean isUserExistsWithViewPermission(User user) {
        String uploadPerms = "";
        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("username",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive());
        Example<User> userExample = Example.of(user, NAME_MATCHER);
        boolean exists = userRepository.exists(userExample);
        if (exists)
            uploadPerms = user.getPermission();
        return (!uploadPerms.isEmpty() && uploadPerms.equals("View") ? true : false);
    }

    @Override
    public String deleteImage(User user, String deleteHash) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization: Client-ID", IMGUR_CLIENTID);
        String resourceUrl = IMGUR_BASE_IMAGE_URL + "/" + deleteHash;
        ExampleMatcher imagehash_matcher = ExampleMatcher.matching().withMatcher("deleteHash",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive());
        Example<UserImage> userImageExample = Example.of(new UserImage(), imagehash_matcher);
        Optional<UserImage> images = userImagesRepository.findOne(userImageExample);
        if (images.isPresent()) {
            userImagesRepository.delete(images.get());
            restTemplate.delete(resourceUrl, headers);
            log.info("The image is deleted successfully");
        } else {
            return "Issue with Image deletion from Imgur and UserImages Table! Please investigate";
        }
        return "Image deleted successfully from Imgur and UserImages Table";
    }

    @Override
    public boolean isUserExistsWithDeletePermission(User user) {
        String deletePerms = "";
        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("username",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive());
        Example<User> userExample = Example.of(user, NAME_MATCHER);
        boolean exists = userRepository.exists(userExample);
        if (exists)
            deletePerms = user.getPermission();
        return (!deletePerms.isEmpty() && deletePerms.equals("Delete") ? true : false);
    }

    @Override
    public String uploadImage(MultipartFile image) {
        HttpURLConnection conn = userUtil.getHttpConnection(IMGUR_CLIENTID, IMGUR_BASE_IMAGE_URL, "POST");
        userUtil.writeToConnection(conn, "image=" + userUtil.toBase64(userUtil.convert(image)));
        return userUtil.getResponse(conn);
    }
}
