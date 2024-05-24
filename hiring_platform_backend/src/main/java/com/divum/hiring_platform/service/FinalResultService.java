package com.divum.hiring_platform.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FinalResultService {

    ResponseEntity<Resource> getExcel(String contestId) throws IOException;

}
