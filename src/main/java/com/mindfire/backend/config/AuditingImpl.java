package com.mindfire.backend.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditingImpl implements AuditorAware<String>{

	@Override
	public Optional<String> getCurrentAuditor() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("System");
        }

        return Optional.of(authentication.getName());
	}

}
