package br.ce.mrb.servicos;

import br.ce.mrb.entidades.Usuario;

public interface EmailService {
	
	public void notificarAtraso(Usuario usuario);

}
