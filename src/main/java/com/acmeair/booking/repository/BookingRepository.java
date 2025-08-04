package com.acmeair.booking.repository;

import java.util.List;
import java.util.Optional;

import com.acmeair.booking.model.Booking;

public interface BookingRepository {
    Optional<Booking> findById(Long id);

    List<Booking> findAll();

    Long save(Booking booking);
}
