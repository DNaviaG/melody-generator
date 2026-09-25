package com.DanielNavia.melody_generator.service;

import com.DanielNavia.melody_generator.Entity.UserEntity;
import com.DanielNavia.melody_generator.dto.user.UserPatchRequest;
import com.DanielNavia.melody_generator.dto.user.UserRegisterRequest;
import com.DanielNavia.melody_generator.dto.user.UserResponse;
import com.DanielNavia.melody_generator.exception.ResourceNotFoundException;
import com.DanielNavia.melody_generator.repository.UserRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los usuarios.
 */
@Service
@AllArgsConstructor
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;

    /**
     * Crea y guarda un nuevo usuario.
     *
     * @param request datos necesarios para registrar el usuario
     * @return usuario creado sin exponer su contraseña
     */
    public UserResponse save(UserRegisterRequest request) {
        UserEntity user = new UserEntity(
                request.username().trim(),
                request.email().trim(),
                request.password(),
                LocalDateTime.now()
        );
        UserEntity savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt()
        );
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios sin exponer sus contraseñas
     */
    public List<UserResponse> findAll() {
        List<UserEntity> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserEntity user : users) {
            UserResponse response = new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getCreatedAt()
            );
            userResponses.add(response);
        }
        return userResponses;
    }

    /**
     * Busca un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado sin exponer su contraseña
     * @throws ResourceNotFoundException si el usuario no existe
     */
    public UserResponse findById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    /**
     * Actualiza parcialmente un usuario existente.
     *
     * @param id identificador del usuario que se quiere actualizar
     * @param request datos que se quieren modificar
     * @return usuario actualizado sin exponer su contraseña
     * @throws RuntimeException si el usuario no existe o algún campo proporcionado está vacío
     */
    public UserResponse partialUpdate(Integer id, UserPatchRequest request) {

        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado con el ID: " + id));

        if (request.username() != null) {
            if (request.username().isBlank()) {
                throw new IllegalArgumentException("El username no puede estar vacío");
            }

            existingUser.setUsername(request.username().trim());
        }

        if (request.email() != null) {
            if (request.email().isBlank()) {
                throw new IllegalArgumentException("El email no puede estar vacío");
            }

            existingUser.setEmail(request.email().trim());
        }

        if (request.password() != null) {
            if (request.password().isBlank()) {
                throw new IllegalArgumentException("La contraseña no puede estar vacía");
            }

            existingUser.setPassword(request.password());
        }

        UserEntity updatedUser = userRepository.save(existingUser);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getCreatedAt()
        );
    }

    /**
     * Elimina un usuario por su identificador.
     *
     * @param id identificador del usuario que se quiere eliminar
     * @throws ResourceNotFoundException si el usuario no existe
     */
    public void deleteById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No se puede eliminar. El usuario no existe con el ID: " + id);
        }
        userRepository.deleteById(id);
    }
}