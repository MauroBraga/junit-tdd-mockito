package br.ce.mrb.servicos;

import static br.ce.mrb.matchers.MatchersProprios.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
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
		
	}
	
	
	
	
	@Test
	public  void deveAlugarFilme() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//cenario 
		
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,5.0);
		List<Filme> filmes = Arrays.asList(filme);
	
		
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		error.checkThat(locacao.getValor(),CoreMatchers.equalTo(5.0));
//		Assert.assertThat(locacao.getValor(),CoreMatchers.not(4.0));
//		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(true));
		error.checkThat(locacao.getDataRetorno(), ehHoje());
		//error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(locacao.getDataLocacao(),1)), CoreMatchers.is(true));
		error.checkThat(locacao.getDataRetorno(),ehHojeComDiferencaDias(1));
		
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
		
		
		
	}
	
	
	
	
	
	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,4.0);
		List<Filme> filmes = Arrays.asList(filme);
	
		//acao
		Locacao locacao  = service.alugarFilme(usuario, filmes);
		
		//verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataLocacao(), Calendar.MONDAY);
		
		Assert.assertTrue(ehSegunda);
		//assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(locacao.getDataRetorno(),caiEm(Calendar.MONDAY));
		assertThat(locacao.getDataRetorno(),caiNumaSegunda());
	}
	
}
