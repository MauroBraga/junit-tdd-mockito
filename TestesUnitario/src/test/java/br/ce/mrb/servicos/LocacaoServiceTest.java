package br.ce.mrb.servicos;



import static br.ce.mrb.builders.FilmeBuilder.umFilme;
import static br.ce.mrb.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.mrb.builders.LocacaoBuilder.umLocacao;
import static br.ce.mrb.builders.UsuarioBuilder.umUsuario;
import static br.ce.mrb.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.mrb.matchers.MatchersProprios.ehHoje;
import static br.ce.mrb.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.mrb.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.mrb.daos.LocacaoDAO;
import br.ce.mrb.entidades.Filme;
import br.ce.mrb.entidades.Locacao;
import br.ce.mrb.entidades.Usuario;
import br.ce.mrb.exceptions.FilmeSemEstoqueException;
import br.ce.mrb.exceptions.LocadoraException;
import br.ce.mrb.servicos.EmailService;
import br.ce.mrb.servicos.LocacaoService;
import br.ce.mrb.servicos.SPCService;
import br.ce.mrb.utils.DataUtils;

public class LocacaoServiceTest {

	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService email;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this); 

	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception{
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{
		//cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//acao
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = umUsuario().agora();
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		//acao
		service.alugarFilme(usuario, null);
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException{
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//acao
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
		
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		//acao
		try {
			service.alugarFilme(usuario, filmes);
		//verificacao
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
		}
		
		verify(spc).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas(){
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		
		List<Locacao> locacoes = Arrays.asList(
				umLocacao().comUsuario(usuario).atrasado().agora()
				,umLocacao().comUsuario(usuario2).agora()
				,umLocacao().atrasado().comUsuario(usuario3).agora()
				,umLocacao().atrasado().comUsuario(usuario3).agora());
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		verify(email,Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(email).notificarAtraso(usuario);
		verify(email,Mockito.never()).notificarAtraso(usuario2);
		verify(email,Mockito.atLeastOnce()).notificarAtraso(usuario3);
		
		Mockito.verifyNoMoreInteractions(email);
	}
	
	
	public void deveTratarErronoSPC() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrofica"));
		
		//acao
		exception.expect(LocadoraException.class);
		//exception.expectMessage("Falha Catastrofica");
		exception.expectMessage("Problemas com SPC, tente novamente");
		
		//verificacao
		service.alugarFilme(usuario, filmes);
		
	}
	
}
