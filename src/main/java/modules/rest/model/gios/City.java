package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "city")
public class City {
  @Id
  @Column(name = "c_id")
  private int id;
  @Column(name = "c_name")
  private String name;
  @Column(name = "c_commune")
  private Commune commune;
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
  @JsonIgnore
  private Set<LocationPointDTO> locationPointDTOSet;
  public City() {
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .add("commune", commune)
        .toString();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Commune getCommune() {
    return commune;
  }

  public void setCommune(Commune commune) {
    this.commune = commune;
  }
}
