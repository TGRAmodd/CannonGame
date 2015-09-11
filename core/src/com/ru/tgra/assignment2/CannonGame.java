package com.ru.tgra.assignment2;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ApplicationListener;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.utils.BufferUtils;

public class CannonGame extends ApplicationAdapter implements InputProcessor{
	
	private FloatBuffer vertexBuffer;

	private FloatBuffer modelMatrixBuffer;
	private FloatBuffer projectionMatrix;

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;

	private int modelMatrixLoc;
	private int projectionMatrixLoc;

	private int colorLoc;
	
	private ModelMatrix modelMatrix;
	
	private float angle = 0.0f;
	
	private float xPos;
	private float yPos;
	private float xAngle;
	private float yAngle;

	private boolean pressed;
	
	private float move_x;
	private float move_y;
	
	ArrayList<Float> rect_x = new ArrayList<Float>();
	ArrayList<Float> rect_y = new ArrayList<Float>();
	
	private float xLine1;
	private float xLine2;
	private float yLine1;
	private float yLine2;
	private boolean zPressed;
	
	InputProcessor inputProcessor;
	
	ArrayList<Line> Lines = new ArrayList<Line>();

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

		mm[0] = 1.0f; mm[4] = 0.0f; mm[8] =  0.0f; mm[12] = 0.0f;
		mm[1] = 0.0f; mm[5] = 1.0f; mm[9] =  0.0f; mm[13] = 0.0f;
		mm[2] = 0.0f; mm[6] = 0.0f; mm[10] = 1.0f; mm[14] = 0.0f;
		mm[3] = 0.0f; mm[7] = 0.0f; mm[11] = 0.0f; mm[15] = 1.0f;

		modelMatrixBuffer = BufferUtils.newFloatBuffer(16);
		modelMatrixBuffer.put(mm);
		modelMatrixBuffer.rewind();

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrixBuffer);

		//COLOR IS SET HERE
		Gdx.gl.glUniform4f(colorLoc, 0, 0, 0, 1);


		Gdx.input.setInputProcessor(this);
		
		CircleGraphic.create(positionLoc);
		CannonGraphic.create(positionLoc);
		RectangleGraphic.create(positionLoc);
		
		modelMatrix = new ModelMatrix();
		modelMatrix.loadIdentityMatrix();
		
		xPos = 0.0f;
		yPos = 0.0f;
		xAngle = 0.0f;
		yAngle = 90.0f;
		pressed = false;
		
		move_x = 0.0f;
		move_y = 0.0f;
		zPressed = false;		
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			if(angle < 70)
			{
				angle += 30.0f * deltaTime;
			}
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			if(angle > -70)
			{
				angle -= 30.0f * deltaTime;
			}
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.Z))
		{
			if ((xPos > (Gdx.graphics.getWidth())/2) || (xPos < -(Gdx.graphics.getWidth())/2) || (yPos > Gdx.graphics.getHeight()) || yPos < 0 || (zPressed == false))
			{
				xAngle = (-angle);
				yAngle = (90.0f - Math.abs(angle));
				xPos = (xAngle / 90.0f) * 20.0f;
				yPos = (yAngle / 90.0f) * 30.0f;
				move_x = xAngle * 2 * deltaTime;
				move_y = yAngle * 2 * deltaTime;
			}
			zPressed = true;
			Line line2 = new Line(100.0f, 200.0f, 200.0f, 100.0f, positionLoc);
			Lines.add(line2);
		}
		
		if (zPressed == true)
		{
			for(int i = 0; i < rect_x.size(); i++)
			{
				float left 	 = rect_x.get(i) - 50;
				float right  = rect_x.get(i) + 50;
				float bottom = rect_y.get(i) - 50;
				float top 	 = rect_y.get(i) + 50;
				if (yPos >= bottom && yPos <= top && xPos >= left && xPos <= right)
				{
					if(yPos - move_y < bottom )
					{
						move_y *= -1;
					}
					else if(Math.abs(xPos - move_x) < Math.abs(left))
					{
						move_x *= -1;
					}
					else if(Math.abs(xPos + move_x) > Math.abs(right))
					{
						move_x *= -1;
					}
					else if(yPos + move_y > top)
					{
						move_y *= -1;
					}
				}
			}
			xPos += move_x;
			yPos += move_y;
		}
	}
	
	private void display()
	{
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//setModelMatrixTranslation(500.0f, 50.0f);
		//setModelMatrixScale(17.1f, 17.1f);
		
		modelMatrix.loadIdentityMatrix();
		modelMatrix.addTranslation(512.0f, 0, 0);

		//Drawing the ball
		Gdx.gl.glUniform4f(colorLoc, 0.7f, 0.2f, 0.4f, 1);
		//modelMatrix.addScale(10.0f, 10.0f, 0);
		modelMatrix.addTranslation(xPos, yPos, 0);
		modelMatrix.setShaderMatrix(modelMatrixLoc);
		CircleGraphic.drawSolidCircle();
		
		modelMatrix.loadIdentityMatrix();
		modelMatrix.addTranslation(512.0f, 0, 0);
		
		//Drawing the cannon
		//modelMatrix.addScale(10.0f, 10.0f, 0);
		Gdx.gl.glUniform4f(colorLoc, 0, 0, 0, 0);
		modelMatrix.addRotationZ(angle);
		modelMatrix.setShaderMatrix(modelMatrixLoc);
		CannonGraphic.drawCannon();		
		
		modelMatrix.loadIdentityMatrix();
		modelMatrix.addTranslation(512.0f, 0, 0);
		Gdx.gl.glUniform4f(colorLoc, 0.5f, 0.3f, 0.6f, 1);
		modelMatrix.addTranslation(300, 200, 0);
		rect_x.add(300.0f);
		rect_y.add(200.0f);
		modelMatrix.setShaderMatrix(modelMatrixLoc);
		RectangleGraphic.drawSolidSquare();
		
		modelMatrix.loadIdentityMatrix();
		modelMatrix.addTranslation(512.0f, 0, 0);
		Gdx.gl.glUniform4f(colorLoc, 0.5f, 0.3f, 0.6f, 1);
		modelMatrix.addTranslation(-300, 200, 0);
		rect_x.add(-300.0f);
		rect_y.add(200.0f);
		modelMatrix.setShaderMatrix(modelMatrixLoc);
		RectangleGraphic.drawSolidSquare();
		
		Gdx.gl.glUniform4f(colorLoc, 0.4f, 0.8f, 0.6f, 1);
		for (int i = 0; i < Lines.size(); i++)
		{
			modelMatrix.loadIdentityMatrix();
			modelMatrix.setShaderMatrix(modelMatrixLoc);
			Lines.get(i).draw();
		}
		
		//Gdx.gl.glVertexAttribPointer(positionLoc, 2, GL20.GL_FLOAT, false, 0, vertexBuffer);	
	}

	@Override
	public void render () {
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();
	}
	


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		xLine1 = screenX;
		yLine1 = screenY;
        System.out.println("touchdown");

		
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		xLine2 = screenX;
		yLine2 = screenY;
        System.out.println("toucup");

		
		Line line = new Line(xLine1, Gdx.graphics.getHeight()-yLine1, xLine2, Gdx.graphics.getHeight()-yLine2, positionLoc);
		Lines.add(line);
		
		return true;
	}
	
	@Override
	public boolean keyUp(int x)
	{
		return false;
	}
	
	@Override
	public boolean keyDown(int x)
	{
		return false;
	}
	
	@Override
	public boolean touchDragged(int x, int y, int z)
	{
		return false;
	}
	
	@Override
	public boolean mouseMoved(int x, int y)
	{
		return false;
	}
	
	@Override
	public boolean keyTyped(char x)
	{
		return false;
	}
	
	@Override
	public boolean scrolled(int x)
	{
		return false;
	}


	private void clearModelMatrix()
	{
		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrixBuffer);
	}
	private void setModelMatrixTranslation(float xTranslate, float yTranslate)
	{
		modelMatrixBuffer.put(12, xTranslate);
		modelMatrixBuffer.put(13, yTranslate);

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrixBuffer);
	}
	private void setModelMatrixScale(float xScale, float yScale)
	{
		modelMatrixBuffer.put(0, xScale);
		modelMatrixBuffer.put(5, yScale);

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrixBuffer);
	}
}