package com.nayaragaspar.gprfid.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionCacheDto {
    private String uri;
    private String count;
    private String errorMessage;
}
