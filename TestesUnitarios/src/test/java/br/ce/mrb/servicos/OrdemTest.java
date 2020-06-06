package br.ce.mrb.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {

	public static int contado = 0;
	
	@Test
	public void inicio() {
		contado =1;
	}
	
	@Test
	public void verifica() {
		Assert.assertEquals(1, contado);
	}

//	@Test
//	public void testGeral() {
//		inicio();
//		verifica();
//	}
}