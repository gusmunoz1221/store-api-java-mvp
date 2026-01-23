package com.store.cart.repository;

import com.store.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity,Long> {
    /**
     * -busca el ID del "CLIENTE" al que le petenece
     */
    Optional<CartEntity> findBySessionId(String sessionId);

    /**
     * -borra los carritos cuya fecha de actualización sea ANTERIOR a la fecha X
    */
     int deleteByUpdatedAtBefore(LocalDateTime cutoffTime);
}
