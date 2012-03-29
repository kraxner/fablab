package net.micalo.persistence.dao;

import javax.persistence.Id;

import net.micalo.persistence.IIdentifiableEntity;
import net.micalo.persistence.dao.BaseDAO;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseDAOTest {
	

	class TestEntity implements IIdentifiableEntity<Long> {
		@Id
		private long id;

		public Long getIdent() {
			return id;
		}

		public void setIdent(Long id) {
			this.id = id; 
		}
	}
	
	class TestDAO extends BaseDAO<TestEntity> {
		private static final long serialVersionUID = 1L;
		public TestDAO() {
			super(TestEntity.class);
		}
		
	}
	@Test
	public void testEntityClass() {
		TestDAO testDAO = new TestDAO();
		
	    assertNotNull(testDAO.entityClass);
	}
}
