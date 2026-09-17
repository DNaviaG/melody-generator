package com.DanielNavia.melody_generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Melody {

    private Scale scale;
    private List<Measure> measures;
}