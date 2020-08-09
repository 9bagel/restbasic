package com.epam.esm.bahlei.restbasic.service.order;

import com.epam.esm.bahlei.restbasic.dao.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.OrderDAO;
import com.epam.esm.bahlei.restbasic.dao.TagDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Order;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.service.impl.OrderServiceImpl;
import com.epam.esm.bahlei.restbasic.service.supplies.AuthoritiesChecker;
import com.epam.esm.bahlei.restbasic.service.validator.OrderValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
  @Mock private OrderDAO orderDAO;
  @Mock private GiftCertificateDAO certificateDAO;
  @Mock private OrderValidator validator;
  @Mock private TagDAO tagDAO;
  @Mock private AuthoritiesChecker authoritiesChecker;
  @InjectMocks private OrderServiceImpl orderService;

  @Test
  void save_ValidOrder_OK() {
    Order validOrder = getValidOrder();
    when(validator.validate(validOrder)).thenReturn(emptyList());
    when(certificateDAO.get(anyLong())).thenReturn(Optional.of(getValidCertificate()));

    assertThatCode(() -> orderService.save(validOrder)).doesNotThrowAnyException();
  }

  @Test
  void save_InvalidOrder_Error() {
    Order invalidOrder = getValidOrder();
    invalidOrder.setUserId(-1);
    when(validator.validate(invalidOrder)).thenReturn(singletonList("error"));

    assertThrows(ValidationException.class, () -> orderService.save(invalidOrder));
  }

  @Test
  void getUserOrders_ExistingUser_OK() {
    Pageable pageable = new Pageable(1, 10);
    List<Order> expected = singletonList(new Order());
    when(orderDAO.getUserOrders(1, pageable)).thenReturn(expected);

    List<Order> actual = orderService.getUserOrders(1, pageable);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void getUserOrders_NonExistingUser_EmptyList() {
    Pageable pageable = new Pageable(1, 10);
    when(orderDAO.getUserOrders(0, pageable)).thenReturn(emptyList());

    List<Order> actual = orderService.getUserOrders(0, pageable);

    assertThat(actual).isEmpty();
  }

  @Test
  void get_ExistingUserIdAndOrderId_OK() {
    Optional<Order> expected = Optional.of(getValidOrder());
    when(orderDAO.get(1, 1)).thenReturn(expected);
    when(certificateDAO.getCertificatesByOrderId(1))
        .thenReturn(singletonList(getValidCertificate()));

    Optional<Order> actual = orderService.get(1, 1);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void get_NonExistingUserId_Empty() {
    when(orderDAO.get(0, 1)).thenReturn(Optional.empty());

    Optional<Order> actual = orderService.get(1, 0);

    assertThat(actual).isEmpty();
  }

  @Test
  void get_NonExistingOrderId_Empty() {
    when(orderDAO.get(1, 0)).thenReturn(Optional.empty());

    Optional<Order> actual = orderService.get(0, 1);

    assertThat(actual).isEmpty();
  }

  private Order getValidOrder() {
    Order order = new Order();
    order.setId(1);
    order.setUserId(1);
    order.setCost(BigDecimal.valueOf(100));
    order.setPurchasedAt(Instant.now());
    order.setCertificates(singletonList(getValidCertificate()));

    return order;
  }

  private GiftCertificate getValidCertificate() {
    GiftCertificate giftCertificate = new GiftCertificate();
    giftCertificate.setId(1L);
    giftCertificate.setDescription("desc");
    giftCertificate.setPrice(BigDecimal.valueOf(100));
    giftCertificate.setDuration(10);
    giftCertificate.setName("name");

    return giftCertificate;
  }
}
