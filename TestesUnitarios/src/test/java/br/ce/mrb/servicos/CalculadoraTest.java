package br.ce.mrb.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.mrb.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {

	private Calculadora calculadora ;
	
	@Before
	public void setUp() {
		calculadora= new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		//cenario
		int a = 5;
		int b =3;
		
		//acao
		int resultado = calculadora.somar(a , b);
		
		
		//verificacao
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		//cenario
		int a = 8;
		int b = 5;
		
		//ação
		int resultado = calculadora.subtrair(a,b);
		
		//verficação
		Assert.assertEquals(3, resultado);
		
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		//cenario 
		int a = 6;
		int b = 3;
		
		//acao
		int resultado = calculadora.divide(a,b);
				
		//verificacao
		Assert.assertEquals(resultado, 2);
		
	}
	
	@Test
	public void divide() throws NaoPodeDividirPorZeroException {
		//cenario 
		String a = "6";
		String b = "3";
		
		//acao
		int resultado = calculadora.divide(a,b);
				
		//verificacao
		Assert.assertEquals(resultado, 2);
		
	}
	
	@Test(expected=NaoPodeDividirPorZeroException.class)
	public void deveLacarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		int a = 6;
		int b = 0;
		//acao
		int resultado = calculadora.divide(a,b);
	}
	
	
	
	
}