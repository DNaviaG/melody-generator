package com.DanielNavia.melody_generator.dto;

import com.DanielNavia.melody_generator.model.Scale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MelodyRequest {
    @NotNull
    private Scale scale;
}