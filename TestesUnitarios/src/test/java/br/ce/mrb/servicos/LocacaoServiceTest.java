package br.ce.mrb.servicos;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import br.ce.mrb.entidades.Filme;
import br.ce.mrb.entidades.Locacao;
import br.ce.mrb.entidades.Usuario;
import br.ce.mrb.utils.DataUtils;

public class LocacaoServiceTest {

	@Test
	public  void teste() {
		//cenario 
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1",2,5.0);
	
		//acao
		Locacao  locacao = service.alugarFilme(usuario, filme);
		
		//verificacao
		Assert.assertThat(locacao.getValor(),CoreMatchers.is(5.0));
		Assert.assertThat(locacao.getValor(),CoreMatchers.not(4.0));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(true));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(locacao.getDataLocacao(),1)), CoreMatchers.is(true));
	}
}
