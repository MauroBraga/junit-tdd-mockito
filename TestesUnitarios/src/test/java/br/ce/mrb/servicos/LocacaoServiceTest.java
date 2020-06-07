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
	public  void deveAlugarFilme() throws Exception {
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
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		//cenario 
		
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",0,5.0);
		List<Filme> filmes = Arrays.asList(filme);
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
	}
	
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{
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
	public void naoDeveAlugarFileSemFilme() throws FilmeSemEstoqueException, LocadoraException{
		//cenario 
		
		Usuario usuario = new Usuario("Usuario 1");
		
		List<Filme> filmes = null;
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");
		//acao
		service.alugarFilme(usuario, filmes);
		
		System.out.println("Forma Nova");
		
	}
	
	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,4.0);
		Filme filme2 = new Filme("Filme 2",2,4.0);
		Filme filme3 = new Filme("Filme 3",2,4.0);
		
		List<Filme> filmes = Arrays.asList(filme,filme2,filme3);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//4+4+3
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(11.0));
	}
	
	@Test
	public void devePagar50PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,4.0);
		Filme filme2 = new Filme("Filme 2",2,4.0);
		Filme filme3 = new Filme("Filme 3",2,4.0);
		Filme filme4 = new Filme("Filme 4",2,4.0);
		
		List<Filme> filmes = Arrays.asList(filme,filme2,filme3,filme4);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//4+4+3+2
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(13.0));
	}
	
	@Test
	public void devePagar25PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,4.0);
		Filme filme2 = new Filme("Filme 2",2,4.0);
		Filme filme3 = new Filme("Filme 3",2,4.0);
		Filme filme4 = new Filme("Filme 4",2,4.0);
		Filme filme5 = new Filme("Filme 5",2,4.0);
		
		List<Filme> filmes = Arrays.asList(filme,filme2,filme3,filme4,filme5);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//4+4+3+2+1 = 14
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(14.0));
	}
	
	@Test
	public void devePagar0PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,4.0);
		Filme filme2 = new Filme("Filme 2",2,4.0);
		Filme filme3 = new Filme("Filme 3",2,4.0);
		Filme filme4 = new Filme("Filme 4",2,4.0);
		Filme filme5 = new Filme("Filme 5",2,4.0);
		Filme filme6 = new Filme("Filme 6",2,4.0);
		
		List<Filme> filmes = Arrays.asList(filme,filme2,filme3,filme4,filme5, filme6);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//4+4+3+2+1 = 14
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(14.0));
	}
	
	
}
