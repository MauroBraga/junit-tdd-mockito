package br.ce.mrb.servicos;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.mrb.entidades.Filme;
import br.ce.mrb.entidades.Locacao;
import br.ce.mrb.entidades.Usuario;
import br.ce.mrb.servicos.LocacaoService;
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
		Assert.assertTrue(locacao.getValor()==5.0D);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(locacao.getDataLocacao(),1)));
	}
}
