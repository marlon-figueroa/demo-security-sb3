package org.app.security.repository.projection;

import org.app.security.domain.Especialidade;
import org.app.security.domain.Medico;
import org.app.security.domain.Paciente;

public interface HistoricoPaciente {

	Long getId();

	Paciente getPaciente();

	String getDataConsulta();

	Medico getMedico();

	Especialidade getEspecialidade();
}
