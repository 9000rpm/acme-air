package com.acmeair.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.acmeair.booking.dto.BookingCreateDto;
import com.acmeair.booking.dto.BookingUpdateDto;
import com.acmeair.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    void createBookingFromDto(BookingCreateDto dto, @MappingTarget Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flightId", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    void updateBookingFromDto(BookingUpdateDto dto, @MappingTarget Booking booking);
}
