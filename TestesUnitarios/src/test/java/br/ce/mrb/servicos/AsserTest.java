package br.ce.mrb.servicos;

import org.junit.Test;

import br.ce.mrb.entidades.Usuario;

import org.junit.*;

public class AsserTest {

	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals("Erro de comparação",1, 1);
		Assert.assertEquals(0.51D, 0.51D, 0.01);
		Assert.assertEquals(Math.PI, 3.14D,0.01);
		
		int i =5;
		Integer i2=5;
				
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());
		
		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("casa", "bola");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		Usuario  u1 = new Usuario("Usuario 1");
		Usuario  u2 = new Usuario("Usuario 1");
		Usuario  u3 = null;
		
		Assert.assertEquals(u1, u2);
		
		//Assert.assertSame(u2, u3);
		Assert.assertNotSame(u1, u3);
		
		Assert.assertNull(u3);
		Assert.assertNotNull(u1);
	}
}
