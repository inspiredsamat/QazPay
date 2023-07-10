package kz.inspiredsamat.qazpay.service;

import kz.inspiredsamat.qazpay.model.User;
import kz.inspiredsamat.qazpay.model.dto.UserDTO;

import java.util.List;

public interface IUserService {

    UserDTO createNewUser(User newUserBody);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();
}
