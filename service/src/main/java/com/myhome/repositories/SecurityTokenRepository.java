package com.myhome.repositories;

import com.myhome.domain.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityTokenRepository extends JpaRepository<SecurityToken, Long> {
}
