package org.qazcodenarxoz.mainmicroservice1.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;


@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
public class MC1Entity {
    @Id
    private Integer id;
    @JsonProperty("session_id")
    private Integer sessionId;
    @JsonProperty("MC1_timestamp")
    private Instant mc1Timestamp;
    @JsonProperty("MC2_timestamp")
    private Instant mc2Timestamp;

    @JsonProperty("MC3_timestamp")
    private Instant mc3Timestamp;

    @JsonProperty("end_timestamp")
    private Instant endTimestamp;

}