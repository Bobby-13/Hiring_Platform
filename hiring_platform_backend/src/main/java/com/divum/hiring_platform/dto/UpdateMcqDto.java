package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.entity.McqImageUrl;
import com.divum.hiring_platform.entity.Options;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMcqDto {
    private List<McqImageUrl> mcqImageUrlS;
    private String question;
    private List<Options> options;
    private String difficulty;
    private String category;
}
