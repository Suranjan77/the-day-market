package com.thedaymarket.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Entity
@Data
public class Category extends Base {
  private String tag;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private Set<Category> children = new HashSet<>();
}
