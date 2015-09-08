package com.ru.tgra.assignment2;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

import com.badlogic.gdx.utils.BufferUtils;

public class CannonGame extends ApplicationAdapter {
	
	private FloatBuffer vertexBuffer;

	private FloatBuffer modelMatrix;
	private FloatBuffer projectionMatrix;

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;

	private int modelMatrixLoc;
	private int projectionMatrixLoc;

	private int colorLoc;

	@Override
	public void create () {

		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/simple2D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/simple2D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	
		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);
	
		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		renderingProgramID = Gdx.gl.glCreateProgram();
	
		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		colorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_color");

		Gdx.gl.glUseProgram(renderingProgramID);

		float[] pm = new float[16];

		pm[0] = 2.0f / Gdx.graphics.getWidth(); pm[4] = 0.0f; pm[8] = 0.0f; pm[12] = -1.0f;
		pm[1] = 0.0f; pm[5] = 2.0f / Gdx.graphics.getHeight(); pm[9] = 0.0f; pm[13] = -1.0f;
		pm[2] = 0.0f; pm[6] = 0.0f; pm[10] = 1.0f; pm[14] = 0.0f;
		pm[3] = 0.0f; pm[7] = 0.0f; pm[11] = 0.0f; pm[15] = 1.0f;

		projectionMatrix = BufferUtils.newFloatBuffer(16);
		projectionMatrix.put(pm);
		projectionMatrix.rewind();
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, projectionMatrix);


		float[] mm = new float[16];

		mm[0] = 1.0f; mm[4] = 0.0f; mm[8] = 0.0f; mm[12] = 0.0f;
		mm[1] = 0.0f; mm[5] = 1.0f; mm[9] = 0.0f; mm[13] = 0.0f;
		mm[2] = 0.0f; mm[6] = 0.0f; mm[10] = 1.0f; mm[14] = 0.0f;
		mm[3] = 0.0f; mm[7] = 0.0f; mm[11] = 0.0f; mm[15] = 1.0f;

		modelMatrix = BufferUtils.newFloatBuffer(16);
		modelMatrix.put(mm);
		modelMatrix.rewind();

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrix);

		//COLOR IS SET HERE
		Gdx.gl.glUniform4f(colorLoc, 0.7f, 0.2f, 0, 1);


		//VERTEX ARRAY IS FILLED HERE
		float[] array = {-50.0f, -50.0f,
						-50.0f, 50.0f,
						50.0f, -50.0f,
						50.0f, 50.0f};

		vertexBuffer = BufferUtils.newFloatBuffer(8);
		vertexBuffer.put(array);
		vertexBuffer.rewind();
		
		CircleGraphic.create(positionLoc);
		CannonGraphic.create(positionLoc);
	}
	
	private void update()
	{
		if(Gdx.input.justTouched())
		{
			//do mouse/touch input stuff
		}
		
		

		//do all updates to the game
	}
	
	private void display()
	{
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		setModelMatrixTranslation(500.0f, 50.0f);
		setModelMatrixScale(17.1f, 17.1f);
		
		//CircleGraphic.drawSolidCircle();
		CannonGraphic.drawCannon();
		//do all actual drawing and rendering here
	}

	@Override
	public void render () {
		
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}


	private void clearModelMatrix()
	{
		modelMatrix.put(0, 1.0f);
		modelMatrix.put(1, 0.0f);
		modelMatrix.put(2, 0.0f);
		modelMatrix.put(3, 0.0f);
		modelMatrix.put(4, 0.0f);
		modelMatrix.put(5, 1.0f);
		modelMatrix.put(6, 0.0f);
		modelMatrix.put(7, 0.0f);
		modelMatrix.put(8, 0.0f);
		modelMatrix.put(9, 0.0f);
		modelMatrix.put(10, 1.0f);
		modelMatrix.put(11, 0.0f);
		modelMatrix.put(12, 0.0f);
		modelMatrix.put(13, 0.0f);
		modelMatrix.put(14, 0.0f);
		modelMatrix.put(15, 1.0f);

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrix);
	}
	private void setModelMatrixTranslation(float xTranslate, float yTranslate)
	{
		modelMatrix.put(12, xTranslate);
		modelMatrix.put(13, yTranslate);

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrix);
	}
	private void setModelMatrixScale(float xScale, float yScale)
	{
		modelMatrix.put(0, xScale);
		modelMatrix.put(5, yScale);

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrix);
	}
}