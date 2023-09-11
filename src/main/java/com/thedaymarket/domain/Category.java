package com.thedaymarket.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.NaturalId;

@Entity
@Data
public class Category extends BaseEntity {
  @Unique private String tag;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private Set<Category> children = new HashSet<>();

  @PrePersist
  @PreUpdate
  private void prepare() {
    this.tag = tag.toLowerCase();
  }
}
