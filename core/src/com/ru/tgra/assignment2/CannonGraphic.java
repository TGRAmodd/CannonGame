package com.ru.tgra.assignment2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.FloatBuffer;

public class CannonGraphic {
	
	private static int vertexPointer;
	
	private static FloatBuffer vertexBuffer;
	
	public static void create(int vertexPointer) {
		
		CannonGraphic.vertexPointer = vertexPointer;
		
		//VERTEX ARRAY IS FILLED HERE
				float[] array = {-20f, 60f,
								-20f, -60f,
								20f, -60f,
								20f, 60f};

				vertexBuffer = BufferUtils.newFloatBuffer(8);
				vertexBuffer.put(array);
				vertexBuffer.rewind();
	}
	
	public static void drawCannon() {
		
		Gdx.gl.glVertexAttribPointer(vertexPointer, 2, GL20.GL_FLOAT, false, 0, vertexBuffer);
		
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, 4);
	}
}