package com.example.tgbot.services;

import com.example.tgbot.domain.User;
import com.example.tgbot.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@Slf4j
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
        if (userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }
        log.info("Nu such user with id: " + id);
        throw new NoSuchElementException();
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
        if (userRepository.findAll() != null){
            return userRepository.findAll();
        }
        log.info("Nu users yet");
        throw new NoSuchElementException();
    }

    public User getUserByName(String name){
        if (userRepository.findUserByName(name).isPresent()){
            return userRepository.findUserByName(name).get();
        }
        log.info("Nu such user with name" + name);
        throw new NoSuchElementException();
    }
}
