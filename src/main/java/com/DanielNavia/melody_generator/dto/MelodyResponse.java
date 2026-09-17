package com.DanielNavia.melody_generator.dto;

import com.DanielNavia.melody_generator.model.Melody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MelodyResponse {
    private Melody melody;
}
