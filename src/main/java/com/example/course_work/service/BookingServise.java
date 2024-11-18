package com.example.course_work.service;

import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Booking;
import com.example.course_work.entity.Client;
import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.PaymentStatusEnum;
import com.example.course_work.exception.BookingNotFound;
import com.example.course_work.mapper.BookingMapper;
import com.example.course_work.repository.AccommodationRepository;
import com.example.course_work.repository.BookingRepository;
import com.example.course_work.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class BookingServise {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public BookingDto getById(Long id){
        Booking booking =bookingRepository.findById(id).orElseThrow();
        return bookingMapper.toDto(booking);
    }
    public BookingDto createBooking(BookingCreationDto bookingCreationDto) {
        Accommodation accommodation = accommodationRepository.findById(bookingCreationDto.accommodationId())
                .orElseThrow(() -> new IllegalArgumentException("Accommodation not found with ID: " + bookingCreationDto.accommodationId()));
        Client client = clientRepository.findById(bookingCreationDto.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + bookingCreationDto.clientId()));
        Booking booking = bookingMapper.toEntity(bookingCreationDto);
        booking.setAccommodation(accommodation);
        booking.setClient(client);
        booking.calculateTotalPrice();
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<BookingDto> getSortedBookings(String sortBy, String order, Pageable pageable) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
        Page<Booking> bookings = bookingRepository.findAll(sortedPageable);
        return bookings.map(booking -> new BookingDto(
                booking.getId(), booking.getCreated(), booking.getBookingDate(), booking.getStatus(),
                booking.getPaymentStatus(), booking.getTotalPrice(), booking.getNotes(),
                booking.getAccommodation().getId(), booking.getAccommodation().getName(),
                booking.getAccommodation().getLocation(),
                booking.getClient().getId(), booking.getClient().getName(),
                booking.getClient().getSurname(), booking.getClient().getEmail(),
                booking.getClient().getPhone(), booking.getNights()
        ));
    }
    @Transactional(readOnly = true)
    public Page<BookingDto> getFilteredBookings(Client client, Date startDate, Date endDate,
                                                BookingStatusEnum status, PaymentStatusEnum paymentStatus,
                                                Double minPrice, Double maxPrice, Pageable pageable) {
        Specification<Booking> specification = Specification.where(null);

        if (client != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("client"), client));
        }
        if (startDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("bookingDate"), startDate));
        }
        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("bookingDate"), endDate));
        }
        if (status != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }
        if (paymentStatus != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
        }
        if (minPrice != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), minPrice));
        }
        if (maxPrice != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), maxPrice));
        }

        Page<Booking> bookings = bookingRepository.findAll(specification, pageable);
        return bookings.map(booking -> new BookingDto(
                booking.getId(), booking.getCreated(), booking.getBookingDate(), booking.getStatus(),
                booking.getPaymentStatus(), booking.getTotalPrice(), booking.getNotes(),
                booking.getAccommodation().getId(), booking.getAccommodation().getName(),
                booking.getAccommodation().getLocation(),
                booking.getClient().getId(), booking.getClient().getName(),
                booking.getClient().getSurname(), booking.getClient().getEmail(),
                booking.getClient().getPhone(), booking.getNights()
        ));
    }
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFound("Booking not found"));
        bookingMapper.partialUpdate(bookingDto, booking);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

}
