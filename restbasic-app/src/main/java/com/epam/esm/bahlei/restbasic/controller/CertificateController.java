package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.controller.criteria.CriteriaMapper;
import com.epam.esm.bahlei.restbasic.controller.dto.GiftCertificateDTO;
import com.epam.esm.bahlei.restbasic.controller.dto.response.ErrorResponse;
import com.epam.esm.bahlei.restbasic.controller.linkmapper.LinkMapper;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.GiftCertificateService;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/")
public class CertificateController {

  private final GiftCertificateService giftCertificateService;
  private final LinkMapper linkMapper;

  @Autowired
  public CertificateController(
      GiftCertificateService giftCertificateService, LinkMapper linkMapper) {
    this.giftCertificateService = giftCertificateService;
    this.linkMapper = linkMapper;
  }

  /**
   * Returns a single Certificate instance for a given certificate id. See {@link #getAll(List,
   * String, String, int, int)} to return a list of certificates and determine individual {@code id}
   * Path [GET /api/certificates/{id}]
   *
   * @param id an id of a certificate
   */
  @PreAuthorize("permitAll()")
  @GetMapping("/certificates/{id}")
  public ResponseEntity<?> getCertificate(@PathVariable long id) {
    Optional<GiftCertificate> optional = giftCertificateService.get(id);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }
    GiftCertificateDTO certificateDTO = toCertificateDTO(optional.get());
    linkMapper.mapLinks(certificateDTO);
    return ok(certificateDTO);
  }

  /**
   * Returns a list of Certificates Path [GET /api/certificates/]
   *
   * @param tagNames (not required) name of tag that Certificate should have
   * @param sortBy (not required) name of sorting field including sorting order
   * @param find (not required) search phrase that certificate should have
   */
  @PreAuthorize("permitAll()")
  @GetMapping("/certificates")
  public ResponseEntity<?> getAll(
      @RequestParam(required = false) List<String> tagNames,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false, defaultValue = "") String find,
      @RequestParam(required = false, defaultValue = "1") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    Criteria criteria = new CriteriaMapper().map(tagNames, sortBy, find);

    return ok(
        giftCertificateService.getAll(criteria, new Pageable(page, size)).stream()
            .map(this::toCertificateDTO)
            .peek(linkMapper::mapLinks)
            .collect(toList()));
  }

  /**
   * Create a certificate Path [POST /api/certificates/]
   *
   * @param certificateDTO a certificate object in json format
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/certificates")
  public ResponseEntity<?> addCertificate(
      @RequestBody GiftCertificateDTO certificateDTO) {
    GiftCertificate giftCertificate = toCertificate(certificateDTO);
    giftCertificateService.save(giftCertificate);

    return created(
            linkTo(methodOn(CertificateController.class).getCertificate(giftCertificate.getId()))
                .toUri())
        .build();
  }

  /**
   * Updates a specific certificate by given certificate id. Should contain JSON body. Path [PUT
   * /api/certificates/{id}]
   *
   * @param certificateDTO certificate object in JSON format
   * @param id certificate id
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping("/certificates/{id}")
  public ResponseEntity<?> updateCertificate(
      @RequestBody GiftCertificateDTO certificateDTO, @PathVariable long id) {
    certificateDTO.id = id;
    if (!giftCertificateService.get(id).isPresent()) {
      return notFound().build();
    }

    giftCertificateService.update(toCertificate(certificateDTO));
    return noContent().build();
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PatchMapping("/certificates/{id}")
  public ResponseEntity<?> patchCertificate(
      @RequestBody GiftCertificateDTO certificateDTO, @PathVariable long id) {

    certificateDTO.id = id;
    Optional<GiftCertificate> certificateOptional = giftCertificateService.get(id);
    if (!certificateOptional.isPresent()) {
      return notFound().build();
    }

    GiftCertificate certificate = certificateOptional.get();
    mergeNewValues(certificateDTO, certificate);

    giftCertificateService.update(certificate);
    return noContent().build();
  }

  /**
   * Deletes a single certificate for a specific certificate id. Path [DELETE
   * /api/certificates/{id}]
   *
   * @param id certificate id
   */
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping("/certificates/{id}")
  public ResponseEntity<ErrorResponse> deleteCertificate(@PathVariable int id) {
    if (giftCertificateService.get(id).isPresent()) {
      giftCertificateService.delete(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  private void mergeNewValues(GiftCertificateDTO dto, GiftCertificate to) {
    GiftCertificate from = toCertificate(dto);
    if (from.getPrice() != null) {
      to.setPrice(from.getPrice());
    }
    if (from.getDescription() != null && !from.getDescription().trim().isEmpty()) {
      to.setDescription(from.getDescription());
    }
    if (from.getName() != null && !from.getName().trim().isEmpty()) {
      to.setName(from.getName());
    }
    if (from.getDuration() != null) {
      to.setDuration(from.getDuration());
    }
    if (from.getTags() != null) {
      to.setTags(from.getTags());
    }
  }

  private GiftCertificateDTO toCertificateDTO(GiftCertificate certificate) {

    return new GiftCertificateDTO(certificate);
  }

  private GiftCertificate toCertificate(GiftCertificateDTO dto) {
    GiftCertificate certificate = new GiftCertificate();
    certificate.setId(dto.id);
    certificate.setDescription(dto.description);
    certificate.setDuration(dto.duration);
    certificate.setName(dto.name);
    certificate.setPrice(dto.price);
    certificate.setTags(
        dto.tags.stream().map(tagDTO -> new Tag(tagDTO.id, tagDTO.name)).collect(toList()));

    return certificate;
  }
}
