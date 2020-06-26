package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.dto.OrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class OrderController {
  @PostMapping("/orders")
  public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {

    return ResponseEntity.created(URI.create("/test2")).build();
  }
}
