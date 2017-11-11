package modules.common.dao;

import modules.rest.model.gios.LocationPointDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LocationPointDao {
  private final HibernateSessionFactory hibernateSessionFactory;

  public LocationPointDao(HibernateSessionFactory hibernateSessionFactory) {
    this.hibernateSessionFactory = hibernateSessionFactory;
  }
  public int save(LocationPointDTO locationPointDTO) {
    assert hibernateSessionFactory != null;
    Session session = hibernateSessionFactory.getInstance().openSession();
    Transaction transaction = session.beginTransaction();
    session.save(locationPointDTO.getCity());
    session.save(locationPointDTO);
    transaction.commit();
    session.close();
    return locationPointDTO.getId();
  }

  public LocationPointDTO getById(int id) {
    assert hibernateSessionFactory != null;
    Session session = hibernateSessionFactory.getInstance().openSession();
    LocationPointDTO locationPointDTO = session.get(LocationPointDTO.class, id);
    session.close();
    return locationPointDTO;
  }

  public List<LocationPointDTO> getAll() {
    Session session = hibernateSessionFactory.getInstance().openSession();
    return session.createQuery("from LocationPointDTO").list();
  }
}
