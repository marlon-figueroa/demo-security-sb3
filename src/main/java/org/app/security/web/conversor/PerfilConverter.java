package org.app.security.web.conversor;

import java.util.ArrayList;
import java.util.List;

import org.app.security.domain.Perfil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PerfilConverter implements Converter<String[], List<Perfil>> {

	@Override
	public List<Perfil> convert(String[] source) {
		List<Perfil> perfil = new ArrayList<>();
		for (String id : source) {
			if (!id.equals("0")) {
				perfil.add(new Perfil(Long.parseLong(id)));
			}
		}
		return perfil;
	}
}
