package modules.common.dao;

import org.hibernate.SessionFactory;

public interface HibernateSessionFactory {
  SessionFactory getInstance();
}
