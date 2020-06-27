package com.epam.esm.bahlei.restbasic.dao.tag;

import com.epam.esm.bahlei.restbasic.config.TestAppConfig;
import com.epam.esm.bahlei.restbasic.dao.certificate.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestAppConfig.class)
@Transactional
class TagDAOImplTest {
  @Autowired private TagDAO tagDAO;
  @Autowired private GiftCertificateDAO certificateDAO;

  @Test
  void get_ExistingTag_OK() {
    Tag expected = new Tag(1, "books");
    tagDAO.save(expected);
    long id = expected.getId();

    Tag actual = tagDAO.get(id).orElse(null);

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void get_NonExistingTag_NotFound() {
    Optional<Tag> actual = tagDAO.get(0);

    assertThat(actual).isEmpty();
  }

  @Test
  void getByName_ExistingName_OK() {
    Tag expected = new Tag(3, "news");
    tagDAO.save(expected);

    Tag actual = tagDAO.getByName("news").orElse(null);

    assertThat(actual).extracting("name").isEqualTo("news");
  }

  @Test
  void getByName_NonExistingName_NotFound() {
    Optional<Tag> actual = tagDAO.getByName("nonExistingName");

    assertThat(actual).isEmpty();
  }

  @Test
  void save_NonExistingTag_OK() {
    Tag tag = new Tag();
    tag.setName("books");

    tagDAO.save(tag);

    Tag actual = tagDAO.get(tag.getId()).orElse(null);
    assertThat(actual).isNotNull();
  }

  @Test
  void getAll_OK() {
    List<Tag> expected = Arrays.asList(new Tag("books"), new Tag("tools"), new Tag("news"));
    expected.forEach(tag -> tagDAO.save(tag));

    List<Tag> actual = tagDAO.getAll();

    assertThat(actual).extracting("name").containsExactly("books", "tools", "news");
  }

  @Test
  void delete_OK() {
    Tag tag = new Tag("books");
    tagDAO.save(tag);

    tagDAO.delete(tag.getId());

    Optional<Tag> actual = tagDAO.get(tag.getId());
    assertThat(actual).isEmpty();
  }

  @Test
  void getCertificateTags_ExistingCertificateId_OK() {
    GiftCertificate certificate = certificate("certificate", new Tag("books"), new Tag("tools"));
    certificate.getTags().forEach(tag -> tagDAO.save(tag));
    certificateDAO.save(certificate);

    certificate
        .getTags()
        .forEach(tag -> tagDAO.saveCertificateTag(certificate.getId(), tag.getId()));
    List<Tag> actual = tagDAO.getCertificateTags(certificate.getId());

    assertThat(actual).usingRecursiveComparison().isEqualTo(certificate.getTags());
  }

  private GiftCertificate certificate(String name, Tag... tags) {
    GiftCertificate certificate =
        new GiftCertificate(
            0, name, "desc", BigDecimal.ONE, OffsetDateTime.now(), OffsetDateTime.now(), 10);
    certificate.setTags(Arrays.asList(tags));

    return certificate;
  }

  @Test
  void getCertificateTags_NonExistingCertificateId_EmptyList() {
    assertThat(tagDAO.getCertificateTags(0)).isEmpty();
  }

  @Test
  void saveCertificateTag_OK() {
    GiftCertificate certificate = certificate("certificate", new Tag("books"), new Tag("tools"));
    certificate.getTags().forEach(tag -> tagDAO.save(tag));
    certificateDAO.save(certificate);

    certificate
        .getTags()
        .forEach(tag -> tagDAO.saveCertificateTag(certificate.getId(), tag.getId()));
    Tag expected = new Tag("news");
    tagDAO.save(expected);

    tagDAO.saveCertificateTag(1, expected.getId());

    assertThat(tagDAO.getCertificateTags(1)).containsOnlyOnce(expected);
  }

  @Test
  void deleteCertificateTag_OK() {
    GiftCertificate certificate = certificate("certificate", new Tag("books"), new Tag("tools"));
    certificate.getTags().forEach(tag -> tagDAO.save(tag));
    certificateDAO.save(certificate);

    certificate
        .getTags()
        .forEach(tag -> tagDAO.saveCertificateTag(certificate.getId(), tag.getId()));
    tagDAO.deleteCertificateTags(1);
    List<Tag> actual = tagDAO.getCertificateTags(1);

    assertThat(actual).isEmpty();
  }
}
