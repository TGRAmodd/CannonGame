package com.ru.tgra.assignment2;

import java.nio.FloatBuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

public class RectangleGraphic {
	private static FloatBuffer vertexBuffer;
	private static int vertexPointer;
	
	public static void create(int vertexPointer) {
		RectangleGraphic.vertexPointer = vertexPointer;
		
		//VERTEX ARRAY IS FILLED HERE
		float[] array = {-50.0f, 50.0f,
		-50.0f, -50.0f,
		50.0f, -50.0f,
		50.0f, 50.0f};
		vertexBuffer = BufferUtils.newFloatBuffer(8);
		vertexBuffer.put(array);
		vertexBuffer.rewind();
	}
	public static void drawSolidSquare() {
		Gdx.gl.glVertexAttribPointer(vertexPointer, 2, GL20.GL_FLOAT,
				false, 0, vertexBuffer);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, 4);
	}
}
