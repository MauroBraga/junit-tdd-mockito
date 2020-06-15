package br.com.mrb.daos;

import java.util.List;

import br.com.mrb.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
}
