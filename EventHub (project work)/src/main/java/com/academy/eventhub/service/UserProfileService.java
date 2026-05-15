package com.academy.eventhub.service;

import com.academy.eventhub.dto.UserProfileRequestDTO;
import com.academy.eventhub.dto.UserProfileResponseDTO;
import com.academy.eventhub.entity.User;
import com.academy.eventhub.entity.UserProfile;
import com.academy.eventhub.exception.BusinessException;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.UserProfileMapper;
import com.academy.eventhub.repository.UserProfileRepository;
import com.academy.eventhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileResponseDTO getProfileByUserId(int userId) {
        return userProfileMapper.toResponseDTO(findProfileOrThrow(userId));
    }

    public UserProfileResponseDTO createProfile(int userId, UserProfileRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + userId));

        if (userProfileRepository.findByUserId(userId).isPresent()) {
            throw new BusinessException("Il profilo per questo utente esiste già");
        }

        UserProfile profile = userProfileMapper.toEntity(dto);
        profile.setUser(user);

        return userProfileMapper.toResponseDTO(userProfileRepository.save(profile));
    }

    public UserProfileResponseDTO updateProfile(int userId, UserProfileRequestDTO dto) {
        UserProfile profile = findProfileOrThrow(userId);
        userProfileMapper.updateEntity(dto, profile);
        return userProfileMapper.toResponseDTO(userProfileRepository.save(profile));
    }

    private UserProfile findProfileOrThrow(int userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profilo non trovato per l'utente con id: " + userId));
    }
}