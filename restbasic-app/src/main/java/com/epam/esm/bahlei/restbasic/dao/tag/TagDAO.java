package com.epam.esm.bahlei.restbasic.dao.tag;

import com.epam.esm.bahlei.restbasic.model.Pageable;
import com.epam.esm.bahlei.restbasic.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDAO {

  Optional<Tag> get(long tagId);

  List<Tag> getAll(Pageable pageable);

  void save(Tag tag);

  void delete(long tagId);

  Optional<Tag> getByName(String tagName);

  void saveCertificateTag(long giftCertificateId, long tagId);

  List<Tag> getCertificateTags(long giftCertificateId);

  void deleteCertificateTags(long certificateId);
}
