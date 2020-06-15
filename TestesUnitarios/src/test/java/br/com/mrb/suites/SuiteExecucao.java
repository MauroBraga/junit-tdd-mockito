package br.com.mrb.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.mrb.servicos.CalculadoraTest;
import br.com.mrb.servicos.CalculoValorLocacaoTest;
import br.com.mrb.servicos.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({
//	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {
	//Remova se puder!
}
