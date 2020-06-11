package br.ce.mrb.servicos;

import static br.ce.mrb.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.mrb.daos.LocacaoDAO;
import br.ce.mrb.entidades.Filme;
import br.ce.mrb.entidades.Locacao;
import br.ce.mrb.entidades.Usuario;
import br.ce.mrb.exceptions.FilmeSemEstoqueException;
import br.ce.mrb.exceptions.LocadoraException;
import br.ce.mrb.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		
		if(usuario==null ) {
			throw new LocadoraException("Usuario Vazio");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme Vazio");
		}

		for(Filme filme : filmes) {
			
			if(filme.getEstoque()==0) {
				throw new FilmeSemEstoqueException();
			}
		}
		
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		Double valorTotal = 0D;
		for(int i=0; i < filmes.size();i++) {
			Filme filme = filmes.get(i);
			Double valorFilme  = filme.getPrecoLocacao();
			
			switch (i) {
				case 2: valorFilme = valorFilme  * 0.75; break;
				case 3: valorFilme = valorFilme  * 0.5; break;
				case 4: valorFilme = valorFilme  * 0.25; break;
				case 5: valorFilme = 0D; break;
				default:
					break;
			}
			
			
			valorTotal+=valorFilme;
		}
		
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY) ) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}

	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao =dao;
	}
	
}