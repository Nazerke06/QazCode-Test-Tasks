package org.qazcodenarxoz.mainmicroservice1.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "mc1_messages")
@NoArgsConstructor
@AllArgsConstructor
public class MC1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("session_id")
    @Column(name = "session_id")
    private Integer sessionId;

    @JsonProperty("MC1_timestamp")
    @Column(name = "MC1_timestamp")
    private Instant mc1Timestamp;

    @JsonProperty("MC2_timestamp")
    @Column(name = "MC2_timestamp")
    private Instant mc2Timestamp;

    @JsonProperty("MC3_timestamp")
    @Column(name = "MC3_timestamp")
    private Instant mc3Timestamp;

    @JsonProperty("end_timestamp")
    @Column(name = "end_timestamp")
    private Instant endTimestamp;

    @Override
    public String toString() {
        return "MC1Entity{" +
                "id=" + id +
                ", sessionId=" + sessionId +
                ", mc1Timestamp=" + mc1Timestamp +
                ", mc2Timestamp=" + mc2Timestamp +
                ", mc3Timestamp=" + mc3Timestamp +
                ", endTimestamp=" + endTimestamp +
                '}';
    }
}