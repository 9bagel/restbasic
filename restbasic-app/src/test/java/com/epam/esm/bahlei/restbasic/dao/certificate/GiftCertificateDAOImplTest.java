package com.epam.esm.bahlei.restbasic.dao.certificate;

import com.epam.esm.bahlei.restbasic.config.TestAppConfig;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;
import com.epam.esm.bahlei.restbasic.service.supplies.Criteria;
import com.epam.esm.bahlei.restbasic.service.supplies.SortColumn;
import com.epam.esm.bahlei.restbasic.service.supplies.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestAppConfig.class)
@Transactional
class GiftCertificateDAOImplTest {

  @Autowired private GiftCertificateDAO certificateDAO;

  @Test
  @Sql("/sql/certificate/data.sql")
  void getAll_NoCriteria_OK() {
    Criteria criteria = new Criteria(emptyList(), emptyList(), "");

    List<GiftCertificate> actual = certificateDAO.getAll(criteria, new Pageable(1, 3));

    assertThat(actual).extracting("id").containsExactly(1L, 2L, 3L);
  }

  @Test
  @Sql("/sql/certificate/data.sql")
  void getAll_SortColumn_OK() {
    SortColumn sortColumn = new SortColumn("name", SortOrder.DESC);
    Criteria criteria = new Criteria(singletonList(sortColumn), emptyList(), "");

    List<GiftCertificate> actual = certificateDAO.getAll(criteria, new Pageable(1, 3));

    assertThat(actual).extracting("id").containsExactly(3L, 2L, 1L);
  }

  @Test
  @Sql("/sql/certificate/data.sql")
  @Sql("/sql/tag/data.sql")
  @Sql("/sql/certificatetag/data.sql")
  void getAll_TagName_OK() {
    Criteria criteria = new Criteria(emptyList(), singletonList("books"), "");

    List<GiftCertificate> actual = certificateDAO.getAll(criteria, new Pageable(1, 10));

    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  @Sql("/sql/certificate/data.sql")
  void getAll_FindPhrase_OK() {
    Criteria criteria = new Criteria(emptyList(), emptyList(), "test");

    List<GiftCertificate> actual = certificateDAO.getAll(criteria, new Pageable(1, 1));

    assertThat(actual).extracting("id").isEqualTo(singletonList(3L));
  }

  private List<GiftCertificate> getValidCertificates() {
    List<GiftCertificate> certificates = new ArrayList<>();

    GiftCertificate certificate1 =
        new GiftCertificate(
            1,
            "certificate1",
            "desc",
            BigDecimal.valueOf(10),
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            5);
    List<Tag> tags = new ArrayList<>();
    tags.add(new Tag(1, "books"));
    tags.add(new Tag(2, "tools"));
    certificate1.setTags(tags);

    GiftCertificate certificate2 =
        new GiftCertificate(
            2,
            "certificate2",
            "desc",
            BigDecimal.valueOf(10),
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            5);
    certificate2.setTags(singletonList(new Tag(3, "news")));

    GiftCertificate certificate3 =
        new GiftCertificate(
            3,
            "certificate3",
            "desc",
            BigDecimal.valueOf(10),
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            5);

    certificates.add(certificate1);
    certificates.add(certificate2);
    certificates.add(certificate3);
    return certificates;
  }

  @Test
  void save_NonExistingCertificate_OK() {
    GiftCertificate expected = getValidCertificate();
    expected.setName("nonExistingName");

    certificateDAO.save(expected);

    GiftCertificate actual = certificateDAO.get(expected.getId()).orElse(null);
    assertThat(actual).isNotNull();
  }

  private GiftCertificate getValidCertificate() {
    return new GiftCertificate(
        1,
        "certificate1",
        "desc",
        BigDecimal.valueOf(10),
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        5);
  }

  @Test
  void get_ExistingCertificate_OK() {
    GiftCertificate certificate = getValidCertificate();
    certificateDAO.save(certificate);

    Optional<GiftCertificate> actual = certificateDAO.get(certificate.getId());

    assertThat(actual).isNotEmpty();
  }

  @Test
  void get_NonExistingCertificate_NotFound() {
    Optional<GiftCertificate> actual = certificateDAO.get(0);

    assertThat(actual).isEmpty();
  }

  @Test
  void delete_ExistingCertificate_OK() {
    GiftCertificate certificate = getValidCertificate();
    certificateDAO.save(certificate);
    long id = certificate.getId();

    certificateDAO.delete(id);

    Optional<GiftCertificate> actual = certificateDAO.get(id);
    assertThat(actual).isEmpty();
  }

  @Test
  void update_OK() {
    GiftCertificate expected = getValidCertificate();
    certificateDAO.save(expected);
    long id = expected.getId();

    expected.setName("newName");
    certificateDAO.update(expected);

    GiftCertificate actual = certificateDAO.get(id).orElse(null);
    assertThat(actual).extracting("name").isEqualTo("newName");
  }
}
