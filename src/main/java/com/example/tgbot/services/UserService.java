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

    public User getUserById(Long id){
        if (userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }
        log.info("Nu such user with id: " + id);
        throw new NoSuchElementException();
    }

    public User getUserByChatId(Long chatId){
        if (userRepository.findByChatId(chatId).isPresent()){
            return userRepository.findByChatId(chatId).get();
        }
        log.info("Nu such user with chatId: " + chatId);
        throw new NoSuchElementException();
    }

    public void deleteUser(User user){
        if (userRepository.findById(user.getId()).isPresent()){
            userRepository.delete(user);
        }
        log.info("Nu such user to delete: " + user);
        throw new NoSuchElementException();
    }

    public void deleteUserById(Long id){
        if (userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
        }
        log.info("Nu such user to delete with id: " + id);
        throw new NoSuchElementException();
    }

    public List<User> getUsers(){
        if (userRepository.findAll() != null){
            return userRepository.findAll();
        }
        log.info("Nu users yet");
        throw new NoSuchElementException();
    }
}
