package net.micalo.persistence;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IdentifiableEntity implements Serializable,
		IIdentifiableEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	protected long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	public void setIdent(Long id) {
		this.id = id;
	}
	
	public Long getIdent() {
		if (id > 0) {
			return Long.valueOf(id);
		} else {
			return null;
		}
	}
}
