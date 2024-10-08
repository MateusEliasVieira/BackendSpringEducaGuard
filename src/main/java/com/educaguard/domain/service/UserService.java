package com.educaguard.domain.service;


import com.educaguard.api.dto.password.NewPasswordInputDTO;
import com.educaguard.domain.model.User;

import java.util.Date;

public interface UserService {

    public User save(User user);

    public User saveUserAfterConfirmedAccountByEmail(String token);

    public User login(User user);

    public User loginRecFacial(String username);

    public User loginWithGoogle(User user);

    public User findUser(Long idUser);

    public Boolean findUser(String username);

    public User findUserByUsername(String username);

    public int updateAttempts(String username);

    public int attemptsUser(String username);

    public Date releaseLogin(String username);

    public Boolean verifyReleaseDateLogin(String username);

    public Date getDateReleaseLogin(String username);

    public void resetAttemptsAndReleaseLogin(String username);

    public User updatePassword(NewPasswordInputDTO newPasswordInputDTO);

}
