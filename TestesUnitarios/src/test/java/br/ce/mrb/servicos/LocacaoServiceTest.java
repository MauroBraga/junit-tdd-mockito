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
import org.mockito.Mockito;

import br.ce.mrb.builders.FilmeBuilder;
import br.ce.mrb.builders.UsuarioBuilder;
import br.ce.mrb.daos.LocacaoDAO;
import br.ce.mrb.daos.LocacaoDaoFake;
import br.ce.mrb.entidades.Filme;
import br.ce.mrb.entidades.Locacao;
import br.ce.mrb.entidades.Usuario;
import br.ce.mrb.exceptions.FilmeSemEstoqueException;
import br.ce.mrb.exceptions.LocadoraException;
import br.ce.mrb.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;
	private SPCService spcService ;
	private LocacaoDAO locacaoDAO;
	
	private static int contador;
	//definição do cotador
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none(); 
	
	
	@Before
	public void setUp() {
		service = new LocacaoService();
//		service.setLocacaoDAO(new LocacaoDaoFake());	
		locacaoDAO = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(Mockito.mock(LocacaoDAO.class)); 
		spcService =Mockito.mock(SPCService.class);
		service.setSpcService(spcService);
		contador++;
		
	}
	
	
	
	
	@Test
	public  void deveAlugarFilme() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//cenario 
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());
	
		
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		error.checkThat(locacao.getValor(),CoreMatchers.equalTo(5.0));
//		Assert.assertThat(locacao.getValor(),CoreMatchers.not(4.0));
//		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(true));
//		error.checkThat(locacao.getDataRetorno(), ehHoje());
		//error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(locacao.getDataLocacao(),1)), CoreMatchers.is(true));
		error.checkThat(locacao.getDataRetorno(),ehHojeComDiferencaDias(1));
		
		//verificacao
	}
	
	//Elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		//cenario 
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();;
		
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora());
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
	}
	
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{
		//cenario 
		
		Usuario usuario =null;
		
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
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
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();;
		
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
		Usuario usuario = UsuarioBuilder.umUsuario().agora();;
		
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
	
		//acao
		Locacao locacao  = service.alugarFilme(usuario, filmes);
		
		//verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataLocacao(), Calendar.MONDAY);
		
		Assert.assertTrue(ehSegunda);
		//assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(locacao.getDataRetorno(),caiEm(Calendar.MONDAY));
		assertThat(locacao.getDataRetorno(),caiNumaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		//Usuario usuario2 = UsuarioBuilder.umUsuario().agora();
		Usuario usuario = UsuarioBuilder.umUsuario().comNome("Mauro").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		Mockito.when(spcService.possuiNegativacao(usuario)).thenReturn(true);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuario Negativado");
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
}
