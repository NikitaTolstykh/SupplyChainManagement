package com.delivery.util.security;

import com.delivery.entity.User;
import com.delivery.util.lookup.UserLookupService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {
    private final UserLookupService userLookupService;

    public CurrentUserService(UserLookupService userLookupService) {
        this.userLookupService = userLookupService;
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userLookupService.findUserByEmail(email);
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}