package org.app.security.domain;

public enum PerfilTipo {
	
	ADMIN(1, "ADMIN"), 
	MEDICO(2, "MEDICO"), 
	PACIENTE(3, "PACIENTE");

	private final long cod;
	private final String desc;

	PerfilTipo(long cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}

	public long getCod() {
		return cod;
	}

	public String getDesc() {
		return desc;
	}
}
