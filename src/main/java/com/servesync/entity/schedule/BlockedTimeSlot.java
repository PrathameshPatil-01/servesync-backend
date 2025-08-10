package com.servesync.entity.schedule;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.provider.Provider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_time_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockedTimeSlot extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "reason")
    private String reason;
}
