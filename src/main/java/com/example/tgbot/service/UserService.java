package com.example.tgbot.service;

import com.example.tgbot.entity.User;
import com.example.tgbot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void setUser(User user){
        userRepository.save(user);
    }

    public boolean existsById(Long id){
        return userRepository.existsById(id);
    }
    public User getUserById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()){
            return userOptional.get();
        }
        log.info("Nu such user with id: " + id);
        return null;
    }

    public boolean deleteUser(User user){
        if (userRepository.findById(user.getChatId()).isPresent()){
            userRepository.delete(user);
            return true;
        }
        log.info("Nu such user to delete: " + user);
        return false;
    }

    public boolean deleteUserById(Long id){
        if (userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        log.info("Nu such user to delete with id: " + id);
        return false;
    }

    public List<User> getUsers(){
        List<User> users = userRepository.findAll();
        if (users != null){
            return users;
        }
        log.info("Nu users yet");
        return null;
    }

    public User getUserByName(String name){
        if (userRepository.findUserByName(name).isPresent()){
            return userRepository.findUserByName(name).get();
        }
        log.info("Nu such user with name" + name);
        return null;
    }
}
