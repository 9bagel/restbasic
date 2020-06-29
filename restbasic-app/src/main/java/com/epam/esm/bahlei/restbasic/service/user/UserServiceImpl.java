package com.epam.esm.bahlei.restbasic.service.user;

import com.epam.esm.bahlei.restbasic.dao.certificate.GiftCertificateDAO;
import com.epam.esm.bahlei.restbasic.dao.user.UserDAO;
import com.epam.esm.bahlei.restbasic.model.GiftCertificate;
import com.epam.esm.bahlei.restbasic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
  public List<User> getAll() {
    List<User> users = userDAO.getAll();

    return users;
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
