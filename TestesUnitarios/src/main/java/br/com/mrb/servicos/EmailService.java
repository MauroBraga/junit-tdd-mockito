package br.com.mrb.servicos;

import br.com.mrb.entidades.Usuario;

public interface EmailService {
	
	public void notificarAtraso(Usuario usuario);

}
