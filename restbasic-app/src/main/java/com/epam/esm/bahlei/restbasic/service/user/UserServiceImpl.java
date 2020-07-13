package com.epam.esm.bahlei.restbasic.service.user;

import com.epam.esm.bahlei.restbasic.dao.certificate.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.user.UserDAO;
import com.epam.esm.bahlei.restbasic.model.User;
import com.epam.esm.bahlei.restbasic.service.utils.ServiceUtils;
import com.epam.esm.bahlei.restbasic.service.validator.GlobalValidator;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.bahlei.restbasic.service.utils.ServiceUtils.*;

@Service
public class UserServiceImpl implements UserService {

  private final UserDAO userDAO;
  private final GiftCertificateDAO certificateDAO;

  @Autowired
  public UserServiceImpl(UserDAO userDAO, GiftCertificateDAO certificateDAO) {
    this.userDAO = userDAO;
    this.certificateDAO = certificateDAO;
  }

  @Override
  public List<User> getAll(int page, int size) {
    List<String> errors = GlobalValidator.validatePagination(page, size);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    long offset = getOffset(page, size);

    return userDAO.getAll(size, offset);
  }

  @Override
  public Optional<User> get(long id) {
    return userDAO.get(id);
  }

  @Override
  public void save(User user) {
    userDAO.save(user);
  }
}
