package org.app.security.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "usuarios", indexes = { @Index(name = "idx_usuario_email", columnList = "email") })
public class Usuario extends AbstractEntity {

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@JsonIgnore
	@Column(name = "senha", nullable = false)
	private String senha;

	@ManyToMany
	@JoinTable(name = "usuarios_tem_perfil", joinColumns = {
			@JoinColumn(name = "usuario_id", referencedColumnName = "id") 
	}, inverseJoinColumns = {
			@JoinColumn(name = "perfil_id", referencedColumnName = "id") 
	})
	private List<Perfil> perfil;

	@Column(name = "activo", nullable = false, columnDefinition = "TINYINT(1)")
	private boolean activo;

	@Column(name = "codigo_verificador", length = 6)
	private String codigoVerificador;

	public Usuario() {
		super();
	}

	public Usuario(Long id) {
		super.setId(id);
	}

	// adiciona perfil a lista
	public void addPerfil(PerfilTipo tipo) {
		if (this.perfil == null) {
			this.perfil = new ArrayList<>();
		}
		this.perfil.add(new Perfil(tipo.getCod()));
	}

	public Usuario(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Perfil> getPerfil() {
		return perfil;
	}

	public void setPerfil(List<Perfil> perfil) {
		this.perfil = perfil;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getCodigoVerificador() {
		return codigoVerificador;
	}

	public void setCodigoVerificador(String codigoVerificador) {
		this.codigoVerificador = codigoVerificador;
	}

}
