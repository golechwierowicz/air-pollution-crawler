package modules.common.dao;

import com.google.common.collect.ImmutableList;
import modules.rest.model.gios.LocationPointDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.*;

public class LocationPointDaoImpl implements LocationPointDao {
  private final HibernateSessionFactory hibernateSessionFactory;
  private final static Logger log = getLogger(LocationPointDaoImpl.class.getName());

  public LocationPointDaoImpl(HibernateSessionFactory hibernateSessionFactory) {
    this.hibernateSessionFactory = hibernateSessionFactory;
  }

  @Override
  public int save(LocationPointDTO locationPointDTO) {
    try(Session session = hibernateSessionFactory.getInstance().openSession()) {
      Transaction transaction = session.beginTransaction();
      session.save(locationPointDTO.getCity());
      session.save(locationPointDTO);
      transaction.commit();
      return locationPointDTO.getId();
    } catch (Exception ex) {
      log.error("Error saving location point", ex);
      return -1;
    }
  }

  @Override
  public LocationPointDTO getById(int id) {
    try(Session session = hibernateSessionFactory.getInstance().openSession()) {
      return session.get(LocationPointDTO.class, id);
    } catch (Exception ex) {
      log.info("Error getting loc point by id", ex);
      return null;
    }
  }

  @Override
  public List<LocationPointDTO> getAll() {
    try(Session session = hibernateSessionFactory.getInstance().openSession()) {
      final List result = session.createQuery("from LocationPointDTO").list();
      return (List<LocationPointDTO>)result;
    } catch (Exception ex) {
      log.error("Error getting all location points", ex);
      return ImmutableList.of();
    }
  }
}
