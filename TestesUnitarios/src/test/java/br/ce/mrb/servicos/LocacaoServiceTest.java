package br.ce.mrb.servicos;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.mrb.entidades.Filme;
import br.ce.mrb.entidades.Locacao;
import br.ce.mrb.entidades.Usuario;
import br.ce.mrb.exceptions.FilmeSemEstoqueException;
import br.ce.mrb.exceptions.LocadoraException;
import br.ce.mrb.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none(); 
	
	@Test
	public  void testeLocacao() throws Exception {
		//cenario 
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,5.0);
	
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);
		error.checkThat(locacao.getValor(),CoreMatchers.equalTo(5.0));
//		Assert.assertThat(locacao.getValor(),CoreMatchers.not(4.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(locacao.getDataLocacao(),1)), CoreMatchers.is(true));
		
		
		//verificacao
	}
	
	//Elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {
		//cenario 
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",0,5.0);
	
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);
	}
	
	
	@Test
	public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException{
		//cenario 
		LocacaoService service = new LocacaoService();
		Usuario usuario =null;
		Filme filme = new Filme("Filme 1",2,5.0);
	
		//acao
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario Vazio"));
		} 
		System.out.println("Forma Robusta");
	}
	
	
	
	@Test
	public void testeLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException{
		//cenario 
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = null;
	
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");
		//acao
		service.alugarFilme(usuario, filme);
		
		System.out.println("Forma Nova");
		
	}
	
}
