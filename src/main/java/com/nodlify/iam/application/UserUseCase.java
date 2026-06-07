package com.nodlify.iam.application;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserUseCase {

    UserData getUserById(String id);

    UserData getUserByEmail(String email);

    Page<UserData> getAllUsers(Pageable pageable);

    UserData createUser(CreateUserCommand command);

    UserData createAdmin(CreateUserCommand command);

    void deleteUser(String id);

    UserData changeDisplayName(String email, ChangeDisplayNameCommand command);

    void changePassword(String email, ChangePasswordCommand command);
}
