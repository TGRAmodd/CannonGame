package com.ru.tgra.assignment2;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

public class GoalGraphic {

	private static int vertexPointer;
	private static int verticesPerCircle = 50;
	private float radius = 50.0f;
	
	private static FloatBuffer vertexBuffer;
	
	public static void create(int vertexPointer) {
		
		GoalGraphic.vertexPointer = vertexPointer;
		
		vertexBuffer = BufferUtils.newFloatBuffer(2 * verticesPerCircle);
		
		//VERTEX ARRAY IS FILLED HERE
		
		double f = 0.0f;
		for (int i = 0; i < verticesPerCircle; i++)
		{
			vertexBuffer.put(2*i, 50*(float)Math.cos(f));
			vertexBuffer.put(2*i + 1, 50*(float)Math.sin(f));
			
			f += 2.0 * Math.PI / (double) verticesPerCircle;
		}
		
		vertexBuffer.rewind();
	}
	
	public static void drawGoal() {
		
		Gdx.gl.glVertexAttribPointer(vertexPointer, 2, GL20.GL_FLOAT, false, 0, vertexBuffer);
		
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, verticesPerCircle);
	}
}
