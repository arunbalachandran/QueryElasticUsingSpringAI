package com.arunbalachandran.queryelasticspringai.entity;


import groovy.transform.ToString;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Table(name = "orders")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false)
    private UUID id;

    private String productName;

    private Integer productQty;

    private Double productPrice;

    private String productDescription;

    @Builder.Default
    @CreationTimestamp
    private LocalDateTime createdTime = LocalDateTime.now();

    @Builder.Default
    @UpdateTimestamp
    private LocalDateTime updatedTime = LocalDateTime.now();
}
