package com.ru.tgra.assignment2;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

public class Line {
	private FloatBuffer vertexBuffer;
	private int vertexPointer;
	
	private float x1, y1, x2, y2;
	
	public Line(float x1, float y1, float x2, float y2, int vertexPointer)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.vertexPointer = vertexPointer;
		
		//VERTEX ARRAY IS FILLED HERE
		float[] array = {x1, y1,
						 x2, y2};
		vertexBuffer = BufferUtils.newFloatBuffer(4);
		vertexBuffer.put(array);
		vertexBuffer.rewind();
	}
	
	public void draw()
	{
		Gdx.gl.glVertexAttribPointer(this.vertexPointer, 2, GL20.GL_FLOAT, 
									false, 0, this.vertexBuffer);
		Gdx.gl.glDrawArrays(GL20.GL_LINES , 0, 4);
	}
	
	public Coordinates getStartingPoint()
	{
		Coordinates start = new Coordinates(this.x1, this.y1);
		return start;
	}
	
	public Coordinates getEndPoint()
	{
		Coordinates end = new Coordinates(this.x2, this.y2);
		return end;
	}
}
