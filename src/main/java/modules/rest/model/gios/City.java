package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "city")
public class City {
  @Id
  @Column(name = "c_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
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

  public City(int id, String name, Commune commune) {
    this.id = id;
    this.name = name;
    this.commune = commune;
  }

  public City(String name) {
    this.name = name;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    City city = (City) o;
    return id == city.id &&
        Objects.equal(name, city.name) &&
        Objects.equal(commune, city.commune);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, commune, locationPointDTOSet);
  }
}
