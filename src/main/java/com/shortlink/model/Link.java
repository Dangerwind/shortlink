package com.shortlink.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "links")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Link<L> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

   // @NotNull
  //  @Column(unique = true)
    private String link;

 //   @NotNull
 //   @Column(unique = true)
    private String original;

    private Long rank;
    private Long count;

  //  @CreatedDate
 //   private LocalDate createdAt;
}
