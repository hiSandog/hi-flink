package com.sandog.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sandog
 * @date 2019/7/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private Long id;
    private String name;

}
