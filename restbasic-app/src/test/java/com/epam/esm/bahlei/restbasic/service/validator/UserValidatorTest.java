package com.epam.esm.bahlei.restbasic.service.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

  @Test
  void testFailed() {

    assertThat(1).isEqualTo(0);
  }
}
