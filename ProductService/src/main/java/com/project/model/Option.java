package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Document(value="opton")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Option {
	
	private String name;
    private String value;
}
