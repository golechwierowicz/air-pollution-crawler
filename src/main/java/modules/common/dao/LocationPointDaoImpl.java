package modules.common.dao;

import modules.rest.model.gios.LocationPointDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LocationPointDaoImpl implements LocationPointDao {
  private final HibernateSessionFactory hibernateSessionFactory;

  public LocationPointDaoImpl(HibernateSessionFactory hibernateSessionFactory) {
    this.hibernateSessionFactory = hibernateSessionFactory;
  }

  @Override
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

  @Override
  public LocationPointDTO getById(int id) {
    assert hibernateSessionFactory != null;
    Session session = hibernateSessionFactory.getInstance().openSession();
    LocationPointDTO locationPointDTO = session.get(LocationPointDTO.class, id);
    session.close();
    return locationPointDTO;
  }

  @Override
  public List<LocationPointDTO> getAll() {
    Session session = hibernateSessionFactory.getInstance().openSession();
    return session.createQuery("from LocationPointDTO").list();
  }
}
