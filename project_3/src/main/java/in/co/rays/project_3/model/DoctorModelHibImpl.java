package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.DoctorDTO;
import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.dto.DoctorDTO;
import in.co.rays.project_3.dto.DoctorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class DoctorModelHibImpl implements DoctorModelInt {

	@Override
	public long add(DoctorDTO dto) throws ApplicationException, DuplicateRecordException {

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
			throw new ApplicationException("Exception in Doctor Add " + e.getMessage());
		} finally {
			session.close();
		}
		/* log.debug("Model add End"); */
		System.out.println("@@@@@@@@@@@@@@@@====" + id);
		return id;

	}

	@Override
	public void update(DoctorDTO dto) throws ApplicationException, DuplicateRecordException {
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
			throw new ApplicationException("Exception in Doctor update" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public void delete(DoctorDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in Doctor Delete" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public DoctorDTO findByPK(long pk) throws ApplicationException {
		Session session = HibDataSource.getSession();
		DoctorDTO dto = null;
		try {
			dto = (DoctorDTO) session.get(DoctorDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting Doctor by pk");
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

			Criteria criteria = session.createCriteria(DoctorDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in  Doctors list");
		} finally {
			session.close();
		}

		return list;

	}

	@Override
	public List search(DoctorDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	@Override
	public List search(DoctorDTO dto, int pageNo, int pageSize) throws ApplicationException {
	    System.out.println("hellllo" + pageNo + "....." + pageSize + "........" + dto.getId());

	    Session session = null;
	    ArrayList<DoctorDTO> list = null;
	    try {
	        session = HibDataSource.getSession();
	        Criteria criteria = session.createCriteria(DoctorDTO.class);

	        if (dto != null) {
	            if (dto.getId() != null) {
	                criteria.add(Restrictions.eq("id", dto.getId()));
	            }
	            if (dto.getName() != null && dto.getName().length() > 0) {
	                criteria.add(Restrictions.like("name", dto.getName() + "%"));
	            }
	            if (dto.getDob() != null && dto.getDob().getTime() > 0) {
	                criteria.add(Restrictions.eq("dob", dto.getDob()));
	            }
	            if (dto.getMobile() != null && dto.getMobile().length() > 0) {
	                criteria.add(Restrictions.like("mobile", dto.getMobile() + "%"));
	            }
	            if (dto.getExpertise() != null && dto.getExpertise().length() > 0) {
	                criteria.add(Restrictions.like("expertise", dto.getExpertise() + "%"));
	            }
	        }

	        if (pageSize > 0) {
	            pageNo = (pageNo - 1) * pageSize;
	            criteria.setFirstResult(pageNo);
	            criteria.setMaxResults(pageSize);
	        }

	        System.out.println("Executing Criteria...");
	        list = (ArrayList<DoctorDTO>) criteria.list();

	    } catch (HibernateException e) {
	        e.printStackTrace();
	        throw new ApplicationException("Exception in Doctor search: " + e.getMessage());
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return list;
	}


}
