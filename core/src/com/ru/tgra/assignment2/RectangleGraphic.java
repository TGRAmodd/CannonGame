package com.ru.tgra.assignment2;

import java.nio.FloatBuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

public class RectangleGraphic {
	//private static FloatBuffer vertexBuffer;
	//private static int vertexPointer;
	private FloatBuffer vertexBuffer;
	private int vertexPointer;
	
	private float x1, y1, x2, y2;
	
	public RectangleGraphic(float x1, float y1, float x2, float y2, int ptr)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.vertexPointer = ptr;
		
		float halfSide = Math.abs(y2-y1) / 2.0f;
		
		//VERTEX ARRAY IS FILLED HERE
		float[] array = {-halfSide, halfSide,
						 -halfSide, -halfSide,
						 halfSide, -halfSide,
						 halfSide, halfSide};
		vertexBuffer = BufferUtils.newFloatBuffer(8);
		vertexBuffer.put(array);
		vertexBuffer.rewind();
	}
	
	/*
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
	*/

	public void drawSolidSquare() {
		Gdx.gl.glVertexAttribPointer(this.vertexPointer, 2, GL20.GL_FLOAT,
				false, 0, this.vertexBuffer);
		Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_FAN, 0, 4);
	}
	
	public float getX1()
	{
		return x1;
	}
	public float getY1()
	{
		return y1;
	}
	public float getX2()
	{
		return x2;
	}
	public float getY2()
	{
		return y2;
	}
	
}
