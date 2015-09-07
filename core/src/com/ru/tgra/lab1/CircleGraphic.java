package com.ru.tgra.lab1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.FloatBuffer;

public class CircleGraphic {
	
	private static int vertexPointer;
	private static int verticesPerCircle = 50;
	
	private static FloatBuffer vertexBuffer;
	
	public static void create(int vertexPointer) {
		
		CircleGraphic.vertexPointer = vertexPointer;
		
		vertexBuffer = BufferUtils.newFloatBuffer(2 * verticesPerCircle);
		
		//VERTEX ARRAY IS FILLED HERE
		/*
		float[] array = {-50.0f, 50.0f,
						-50.0f, -50.0f,
						50.0f, -50.0f,
						50.0f, 50.0f};
						
		float[] array = new float[2*verticesPerCircle];
		vertexBuffer = BufferUtils.newFloatBuffer(8);
		vertexBuffer.put(array);
		*/
		double f = 0.0f;
		for (int i = 0; i < verticesPerCircle; i++)
		{
			vertexBuffer.put(2*i, (float)Math.cos(f));
			vertexBuffer.put(2*i + 1, (float)Math.sin(f));
			
			f += 2.0 * Math.PI / (double) verticesPerCircle;
		}
		
		
		vertexBuffer.rewind();
	}
	
	public static void drawSolidCircle() {
		
		Gdx.gl.glVertexAttribPointer(vertexPointer, 2, GL20.GL_FLOAT, false, 0, vertexBuffer);
		
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, verticesPerCircle);
	}
	
	public static void drawOutlineCircle() {
		Gdx.gl.glVertexAttribPointer(vertexPointer, 2, GL20.GL_FLOAT, false, 0, vertexBuffer);
		
		Gdx.gl.glDrawArrays(GL20.GL_LINE_LOOP, 0, verticesPerCircle);
	}
}
