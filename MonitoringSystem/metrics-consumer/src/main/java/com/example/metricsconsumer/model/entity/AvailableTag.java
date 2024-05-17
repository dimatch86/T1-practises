package com.example.metricsconsumer.model.entity;

import com.example.model.AvailableTagEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "available_tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tag;
    @ElementCollection
    @CollectionTable(name = "available_tag_values")
    @Column(name = "value")
    private List<String> values;

    public static AvailableTag availableTagOf(AvailableTagEvent event) {
        return AvailableTag.builder()
                .tag(event.getTag())
                .values(event.getValues())
                .build();
    }
}
