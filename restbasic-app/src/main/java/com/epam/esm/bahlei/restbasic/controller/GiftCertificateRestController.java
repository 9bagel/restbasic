package com.epam.esm.bahlei.restbasic.controller;

import com.epam.esm.bahlei.restbasic.config.exception.response.ErrorResponse;
import com.epam.esm.bahlei.restbasic.controller.dto.GiftCertificateDTO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.service.certificate.GiftCertificateService;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/")
public class GiftCertificateRestController {

  private final GiftCertificateService giftCertificateService;

  @Autowired
  public GiftCertificateRestController(GiftCertificateService giftCertificateService) {
    this.giftCertificateService = giftCertificateService;
  }

  /**
   * Returns a single Certificate instance for a given certificate id. See {@link #getAll(String,
   * String, String)} to return a list of certificates and determine individual {@code id} Path [GET
   * /api/certificates/{id}]
   *
   * @param id an id of a certificate
   */
  @GetMapping("/certificates/{id}")
  public ResponseEntity<?> getCertificate(@PathVariable long id) {
    Optional<GiftCertificate> optional = giftCertificateService.get(id);
    if (!optional.isPresent()) {
      return status(HttpStatus.NOT_FOUND).build();
    }

    return ok(toCertificateDTO(optional.get()));
  }

  /**
   * Returns a list of Certificates Path [GET /api/certificates/]
   *
   * @param tagName (not required) name of tag that Certificate should have
   * @param sortBy (not required) name of sorting field including sorting order
   * @param find (not required) search phrase that certificate should have
   */
  @GetMapping("/certificates")
  public ResponseEntity<?> getAll(
      @RequestParam(required = false) String tagName,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String find) {
    Criteria criteria = new CriteriaParser().parse(tagName, sortBy, find);

    return ok(
        giftCertificateService.getAll(criteria).stream()
            .map(this::toCertificateDTO)
            .collect(toList()));
  }

  /**
   * Create a certificate Path [POST /api/certificates/]
   *
   * @param certificateDTO a certificate object in json format
   */
  @PostMapping("/certificates")
  public ResponseEntity<?> addCertificate(
      @RequestBody GiftCertificateDTO certificateDTO, HttpServletRequest httpServletRequest) {
    GiftCertificate giftCertificate = toCertificate(certificateDTO);
    giftCertificateService.save(giftCertificate);

    return created(
            URI.create(
                httpServletRequest.getRequestURL().append(giftCertificate.getId()).toString()))
        .build();
  }

  /**
   * Updates a specific certificate by given certificate id. Should contain JSON body. Path [PUT
   * /api/certificates/{id}]
   *
   * @param certificateDTO certificate object in JSON format
   * @param id certificate id
   */
  @PutMapping("/certificates/{id}")
  public ResponseEntity<?> updateCertificate(
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

  /**
   * Deletes a single certificate for a specific certificate id. Path [DELETE
   * /api/certificates/{id}]
   *
   * @param id certificate id
   */
  @DeleteMapping("/certificates/{id}")
  public ResponseEntity<ErrorResponse> deleteCertificate(@PathVariable int id) {
    if (giftCertificateService.get(id).isPresent()) {
      giftCertificateService.delete(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  private GiftCertificateDTO toCertificateDTO(GiftCertificate certificate) {

    return new GiftCertificateDTO(certificate);
  }

  private GiftCertificate toCertificate(GiftCertificateDTO dto) {
    GiftCertificate certificate = new GiftCertificate();
    certificate.setId(dto.id);
    certificate.setCreatedAt(dto.createdAt);
    certificate.setDescription(dto.description);
    certificate.setDuration(dto.duration);
    certificate.setModifiedAt(dto.modifiedAt);
    certificate.setName(dto.name);
    certificate.setPrice(dto.price);
    certificate.setTags(dto.tags);

    return certificate;
  }
}
