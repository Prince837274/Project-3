package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.PositionDTO;
import in.co.rays.project_3.dto.PositionDTO;
import in.co.rays.project_3.dto.PositionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class PositionModelHibImpl  implements PositionModelInt {

	@Override
	public long add(PositionDTO dto) throws ApplicationException, DuplicateRecordException {
		
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long id;
		try {
			tx = session.beginTransaction();
			id = (long) session.save(dto);
			dto.getId();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in Position Add " + e.getMessage());
		} finally {
			session.close();
		}
		/* log.debug("Model add End"); */ 
		System.out.println("@@@@@@@@@@@@@@@@===="+id);
		return id;

	
	}

	@Override
	public void update(PositionDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = HibDataSource.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Position update" + e.getMessage());
		} finally {
			session.close();
		}
		
	}

	@Override
	public void delete(PositionDTO dto) throws ApplicationException {
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Position Delete" + e.getMessage());
		} finally {
			session.close();
		}

	
		
	}

	@Override
	public PositionDTO findByPK(long pk) throws ApplicationException {
		Session session = HibDataSource.getSession();
		PositionDTO dto = null;
		try {
			dto = (PositionDTO) session.get(PositionDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting Position by pk");
		} finally {
			session.close();
		}

		return dto;
	} 
		
	
	

	@Override
	public List list() throws ApplicationException {
		return list(0, 0);
	}
	
	

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session = HibDataSource.getSession();
		List list = null;
		try {

			Criteria criteria = session.createCriteria(PositionDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in  Positions list");
		} finally {
			session.close();
		}

		return list;

	}


	@Override
	public List search(PositionDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}
	

	@Override
	public List search(PositionDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		ArrayList<PositionDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(PositionDTO.class);
			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}
				
				if (dto.getOpeningDate() != null && dto.getOpeningDate().getTime() > 0) {
					criteria.add(Restrictions.eq("openingDate", dto.getOpeningDate()));
				}
				if (dto.getRequiredExperience() != null && dto.getRequiredExperience().length()>0) {
					criteria.add(Restrictions.like("requiredExperience", dto.getRequiredExperience()+"%"));
				}
				if (dto.getCondition() != null && dto.getCondition().length()>0) {
					criteria.add(Restrictions.like("condition", dto.getCondition()+"%"));
				}

			}
			// if pageSize is greater than 0
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<PositionDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Position search");
		} finally {
			session.close();
		}

		return list;

	}

}
	


