package com.example.course_work.service;

import com.example.course_work.dto.BookingCreationDto;
import com.example.course_work.dto.BookingDto;
import com.example.course_work.entity.Accommodation;
import com.example.course_work.entity.Booking;
import com.example.course_work.entity.Client;
import com.example.course_work.enums.BookingStatusEnum;
import com.example.course_work.enums.PaymentStatusEnum;
import com.example.course_work.exception.BookingNotFound;
import com.example.course_work.exception.ClientNotFound;
import com.example.course_work.exception.TourNotFound;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@AllArgsConstructor
@Transactional
public class BookingServise {
    private static final Logger logger = LoggerFactory.getLogger(BookingServise.class);

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public BookingDto getById(Long id) {
        logger.info("Fetching booking by ID: {}", id);
        try {
            Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFound("Booking not found"));
            logger.info("Booking with ID {} fetched successfully", id);
            return bookingMapper.toDto(booking);
        } catch (BookingNotFound e) {
            logger.warn("Booking with ID {} not found", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while fetching booking by ID: {}", id, e);
            throw e;
        }
    }

    public BookingDto createBooking(BookingCreationDto bookingCreationDto) {
        logger.info("Creating booking with client ID: {} and accommodation ID: {}", bookingCreationDto.clientId(), bookingCreationDto.accommodationId());
        try {
            Accommodation accommodation = accommodationRepository.findById(bookingCreationDto.accommodationId())
                    .orElseThrow(() -> new IllegalArgumentException("Accommodation not found with ID: " + bookingCreationDto.accommodationId()));
            logger.info("Accommodation with ID {} found", bookingCreationDto.accommodationId());

            Client client = clientRepository.findById(bookingCreationDto.clientId())
                    .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + bookingCreationDto.clientId()));
            logger.info("Client with ID {} found", bookingCreationDto.clientId());

            Booking booking = bookingMapper.toEntity(bookingCreationDto);
            booking.setAccommodation(accommodation);
            booking.setClient(client);
            booking.calculateTotalPrice();

            Booking savedBooking = bookingRepository.save(booking);
            logger.info("Booking with ID {} created successfully", savedBooking.getId());

            return bookingMapper.toDto(savedBooking);
        } catch (Exception e) {
            logger.error("Error while creating booking", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<BookingDto> getAllBookings(Pageable pageable) {
        logger.info("Fetching bookings with pagination");
        try {
            Page<BookingDto> bookings = bookingRepository.findAll(pageable)
                    .map(bookingMapper::toDto);
            logger.info("Successfully fetched {} bookings on page {}", bookings.getNumberOfElements(), pageable.getPageNumber());
            return bookings;
        } catch (Exception e) {
            logger.error("Error while fetching paginated bookings", e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public Page<BookingDto> getSortedBookings(String sortBy, String order, Pageable pageable) {
        logger.info("Fetching sorted bookings by field '{}' in '{}' order", sortBy, order);
        try {
            Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));

            Page<Booking> bookings = bookingRepository.findAll(sortedPageable);
            logger.info("Fetched {} sorted bookings", bookings.getTotalElements());

            return bookings.map(booking -> new BookingDto(
                    booking.getId(), booking.getCreated(), booking.getBookingDate(), booking.getStatus(),
                    booking.getPaymentStatus(), booking.getTotalPrice(), booking.getNotes(),
                   booking.getAccommodation().getName(),
                    booking.getAccommodation().getLocation(),
                    booking.getClient().getName(),
                    booking.getClient().getSurname(), booking.getClient().getEmail(),
                    booking.getClient().getPhone(), booking.getNights()
            ));
        } catch (Exception e) {
            logger.error("Error while fetching sorted bookings", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<BookingDto> getFilteredBookings(Client client, Date startDate, Date endDate,
                                                BookingStatusEnum status, PaymentStatusEnum paymentStatus,
                                                Double minPrice, Double maxPrice, Pageable pageable) {
        logger.info("Fetching filtered bookings with client ID: {}, startDate: {}, endDate: {}, status: {}, paymentStatus: {}, minPrice: {}, maxPrice: {}",
                client != null ? client.getId() : "null", startDate, endDate, status, paymentStatus, minPrice, maxPrice);

        try {
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
            logger.info("Fetched {} filtered bookings", bookings.getTotalElements());

            return bookings.map(booking -> new BookingDto(
                    booking.getId(), booking.getCreated(), booking.getBookingDate(), booking.getStatus(),
                    booking.getPaymentStatus(), booking.getTotalPrice(), booking.getNotes(),
                  booking.getAccommodation().getName(),
                    booking.getAccommodation().getLocation(),
                     booking.getClient().getName(),
                    booking.getClient().getSurname(), booking.getClient().getEmail(),
                    booking.getClient().getPhone(), booking.getNights()
            ));
        } catch (Exception e) {
            logger.error("Error while fetching filtered bookings", e);
            throw e;
        }
    }

    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        logger.info("Updating booking with ID: {}", id);
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new BookingNotFound("Booking not found"));
            logger.info("Booking with ID {} found", id);

            bookingMapper.partialUpdate(bookingDto, booking);
            Booking updatedBooking = bookingRepository.save(booking);
            logger.info("Booking with ID {} updated successfully", id);

            return bookingMapper.toDto(updatedBooking);
        } catch (BookingNotFound e) {
            logger.warn("Booking with ID {} not found", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating booking with ID: {}", id, e);
            throw e;
        }
    }
    @Transactional
    public void deleteBooking(Long id) {
        logger.info("Attempting to mark Booking with ID: {} as deleted", id);
        try {
            Booking booking = bookingRepository.findById(id).orElseThrow(() -> {
                logger.warn("Booking with ID: {} not found", id);
                return new BookingNotFound("Booking with ID " + id + " not found.");
            });
            booking.setDeleted(true);
            bookingRepository.save(booking);
            logger.info("Booking with ID: {} marked as deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error occurred while marking Booking with ID: {} as deleted", id, e);
            throw e;
        }
    }

}
