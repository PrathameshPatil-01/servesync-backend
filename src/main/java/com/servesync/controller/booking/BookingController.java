package com.servesync.controller.booking;

import com.servesync.dto.user.*;
import com.servesync.dto.address.*;
import com.servesync.dto.booking.*;
import com.servesync.dto.payment.*;
import com.servesync.dto.review.*;
import com.servesync.dto.provider.*;
import com.servesync.dto.verification.*;
import com.servesync.dto.audit.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    @GetMapping
    @Operation(summary = "Get all bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(new BookingResponse());
    }

    @PostMapping
    @Operation(summary = "Create booking")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new BookingResponse());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update booking status")
    public ResponseEntity<String> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok("Booking status updated");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel booking")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok("Booking cancelled");
    }
}