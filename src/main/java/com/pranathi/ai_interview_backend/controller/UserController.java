
@PostMapping("/login")
public UserResponse login(@RequestBody User user) {

    User foundUser = userRepository
            .findByEmail(user.getEmail())
            .orElse(null);

    if (foundUser == null || user.getPassword() == null) {
        return null;
    }

    String storedPassword = foundUser.getPassword();
    String submittedPassword = user.getPassword();

    if (storedPassword == null) {
        return null;
    }

    boolean passwordMatches;

    if (storedPassword.startsWith("$2a$")
            || storedPassword.startsWith("$2b$")
            || storedPassword.startsWith("$2y$")) {

        passwordMatches = passwordService.verifyPassword(
                submittedPassword,
                storedPassword
        );

    } else {
        // Temporary compatibility for existing plain-text passwords
        passwordMatches = storedPassword.equals(submittedPassword);

        if (passwordMatches) {
            foundUser.setPassword(
                    passwordService.hashPassword(submittedPassword)
            );
            userRepository.save(foundUser);
        }
    }

    if (!passwordMatches) {
        return null;
    }

    return toUserResponse(foundUser);
}
