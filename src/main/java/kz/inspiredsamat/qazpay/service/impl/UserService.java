package kz.inspiredsamat.qazpay.service.impl;

import kz.inspiredsamat.qazpay.model.User;
import kz.inspiredsamat.qazpay.model.dto.UserDTO;
import kz.inspiredsamat.qazpay.repository.UserRepository;
import kz.inspiredsamat.qazpay.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createNewUser(User newUserBody) {
        newUserBody.setPassword(passwordEncoder.encode(newUserBody.getPassword()));
        User createdUser = userRepository.save(newUserBody);
        log.info("User with id {} was created", createdUser.getId());
        return entityToDTO(createdUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            log.error("User with id {} does not exist", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return entityToDTO(optionalUser.get());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("All users from database were retrieved");
        return userRepository.findAll()
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO entityToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setUsername(user.getUsername());

        return userDTO;
    }
}
