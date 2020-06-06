package br.ce.mrb.servicos;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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

	private LocacaoService service;
	private static int contador;
	//definição do cotador
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none(); 
	
	
	@Before
	public void setUp() {
		service = new LocacaoService();
		contador++;
		System.out.println(contador);
	}
	
	
	
	
	@Test
	public  void testeLocacao() throws Exception {
		//cenario 
		
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,5.0);
		List<Filme> filmes = Arrays.asList(filme);
	
		System.out.println("Teste!");
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
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
		
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",0,5.0);
		List<Filme> filmes = Arrays.asList(filme);
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
	}
	
	
	@Test
	public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueException{
		//cenario 
		
		Usuario usuario =null;
		Filme filme = new Filme("Filme 1",2,5.0);
		List<Filme> filmes = Arrays.asList(filme);
		//acao
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario Vazio"));
		} 
		System.out.println("Forma Robusta");
	}
	
	
	
	@Test
	public void testeLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException{
		//cenario 
		
		Usuario usuario = new Usuario("Usuario 1");
		
		List<Filme> filmes = null;
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");
		//acao
		service.alugarFilme(usuario, filmes);
		
		System.out.println("Forma Nova");
		
	}
	
}
