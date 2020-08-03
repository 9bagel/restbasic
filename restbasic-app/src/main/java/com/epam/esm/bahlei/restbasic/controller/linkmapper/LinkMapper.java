package com.epam.esm.bahlei.restbasic.controller.linkmapper;

import com.epam.esm.bahlei.restbasic.controller.CertificateController;
import com.epam.esm.bahlei.restbasic.controller.TagController;
import com.epam.esm.bahlei.restbasic.controller.UserController;
import com.epam.esm.bahlei.restbasic.controller.dto.GiftCertificateDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.OrderDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.TagDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.UserDTO;
import com.epam.esm.bahlei.restbasic.security.jwt.JwtUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.bahlei.restbasic.model.Role.ADMIN;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LinkMapper {

  public void mapLinks(TagDTO tagDTO) {
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    tagDTO.add(linkTo(methodOn(TagController.class).getTag(tagDTO.id)).withSelfRel());

    if (user.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN.toString()))) {
      tagDTO.add(linkTo(methodOn(TagController.class).deleteTag(tagDTO.id)).withRel("delete"));
      tagDTO.add(linkTo(methodOn(TagController.class).addTag(tagDTO)).withRel("add"));
    }
  }

  public void mapLinks(List<TagDTO> tags) {
    tags.forEach(this::mapLinks);
  }

  public void mapLinks(GiftCertificateDTO certificateDTO) {
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    certificateDTO.add(
        linkTo(methodOn(CertificateController.class).getCertificate(certificateDTO.id))
            .withSelfRel());

    if (user.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN.toString()))) {
      certificateDTO.add(
          (linkTo(methodOn(CertificateController.class).addCertificate(certificateDTO))
              .withRel("add")));

      certificateDTO.add(
          (linkTo(
                  methodOn(CertificateController.class)
                      .updateCertificate(certificateDTO, certificateDTO.id))
              .withRel("replace")));

      certificateDTO.add(
          (linkTo(
                  methodOn(CertificateController.class)
                      .patchCertificate(certificateDTO, certificateDTO.id))
              .withRel("update")));
    }

    if (certificateDTO.tags != null && !certificateDTO.tags.isEmpty()) {

      mapLinks(certificateDTO.tags);
    }
  }

  public void mapLinks(UserDTO userDTO) {
    userDTO.add(linkTo(methodOn(UserController.class).getUser(userDTO.id)).withSelfRel());
    linkTo(methodOn(UserController.class).getUserOrders(userDTO.id, 1, 10)).withRel("orders");

    userDTO.add(
        linkTo(methodOn(UserController.class).getFavouriteCertificate(userDTO.id))
            .withRel("favourite"));

    linkTo(methodOn(UserController.class).createOrder(userDTO.id, new OrderDTO())).withRel("add");
  }

  public void mapLinks(OrderDTO orderDTO) {
    orderDTO.add(
        linkTo(methodOn(UserController.class).getOrder(orderDTO.userId, orderDTO.id))
            .withSelfRel());

    orderDTO.certificates.forEach(
        certificateRefDTO -> {
          GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
          certificateDTO.id = certificateRefDTO.id;
          mapLinks(certificateDTO);
        });
  }
}
