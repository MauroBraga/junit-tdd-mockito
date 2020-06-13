package br.ce.mrb.daos;

import java.util.List;

import br.ce.mrb.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
}
