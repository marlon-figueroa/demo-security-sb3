package org.app.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "perfil")
public class Perfil extends AbstractEntity {

	@Column(name = "descricao", nullable = false, unique = true)
	private String desc;

	public Perfil() {
		super();
	}

	public Perfil(Long id) {
		super.setId(id);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
