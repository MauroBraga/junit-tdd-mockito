package br.ce.mrb.servicos;

import static br.ce.mrb.utils.DataUtils.adicionarDias;

import java.util.Date;

import br.ce.mrb.entidades.Filme;
import br.ce.mrb.entidades.Locacao;
import br.ce.mrb.entidades.Usuario;
import br.ce.mrb.exceptions.FilmeSemEstoqueException;
import br.ce.mrb.exceptions.LocadoraException;

public class LocacaoService {
	
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws FilmeSemEstoqueException, LocadoraException {
		
		if(usuario==null ) {
			throw new LocadoraException("Usuario Vazio");
		}
		
		if(filme == null) {
			throw new LocadoraException("Filme Vazio");
		}

		if(filme.getEstoque()==0) {
			throw new FilmeSemEstoqueException();
		}
		
		
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	
}