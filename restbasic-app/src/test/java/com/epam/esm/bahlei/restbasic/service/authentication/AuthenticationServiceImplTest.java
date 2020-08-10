package com.epam.esm.bahlei.restbasic.service.authentication;

import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Test
    void getToken_NonExistingId_EmptyOptional() {

    }
}
