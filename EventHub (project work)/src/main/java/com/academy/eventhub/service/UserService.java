package com.academy.eventhub.service;

import com.academy.eventhub.dto.UserResponseDTO;
import com.academy.eventhub.entity.User;
import com.academy.eventhub.exception.BusinessException;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.UserMapper;
import com.academy.eventhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    public UserResponseDTO getUserById(int id) {
        return userMapper.toResponseDTO(findUserOrThrow(id));
    }

    public UserResponseDTO promoteUser(int id) {
        User user = findUserOrThrow(id);

        if (user.getRole() != User.Role.USER) {
            throw new BusinessException("Solo un utente con ruolo USER può essere promosso a ORGANIZER");
        }

        user.setRole(User.Role.ORGANIZER);
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO banUser(int id) {
        User user = findUserOrThrow(id);

        if (user.getRole() == User.Role.ADMIN) {
            throw new BusinessException("Non puoi bannare un amministratore");
        }
        if (user.getStatus() == User.Status.BANNED) {
            throw new BusinessException("L'utente è già bannato");
        }

        user.setStatus(User.Status.BANNED);
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO reactivateUser(int id) {
        User user = findUserOrThrow(id);

        if (user.getStatus() == User.Status.ACTIVE) {
            throw new BusinessException("L'utente è già attivo");
        }

        user.setStatus(User.Status.ACTIVE);
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    private User findUserOrThrow(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));
    }
}