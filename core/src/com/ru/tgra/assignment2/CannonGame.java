/* Authors: Jörundur Jörundsson & Sverrir Páll Sverrisson
 * Course: Computer Graphics (TGRA)
 * Teacher: Kári Halldórsson
 * */

package com.ru.tgra.assignment2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.utils.BufferUtils;

public class CannonGame extends ApplicationAdapter implements InputProcessor{
	
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
	
	private float angle;
	private float ballPosX;
	private float ballPosY;
	private float xAngle;
	private float yAngle;

	private boolean drawingLine;
	private boolean drawingRect;
	
	private float move_x;
	private float move_y;
	
	private float xLine1;
	private float xLine2;
	private float yLine1;
	private float yLine2;
	private boolean zPressed;
	
	private float xRect1;
	private float xRect2;
	private float yRect1;
	private float yRect2;
	
	private float mousePosX;
	private float mousePosY;
	private float goalPosX;
	private float goalPosY;
	
	InputProcessor inputProcessor;
	
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<RectangleGraphic> rectangles = new ArrayList<RectangleGraphic>();

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
		GoalGraphic.create(positionLoc);
		
		modelMatrix = new ModelMatrix();
		modelMatrix.loadIdentityMatrix();
		
		ballPosX = 0.0f;
		ballPosY = 0.0f;
		xAngle = 0.0f;
		yAngle = 90.0f;
		angle = 0.0f;
		
		move_x = 0.0f;
		move_y = 0.0f;
		zPressed = false;
		drawingLine = false;
		mousePosX = 0.0f;
		mousePosY = 0.0f;
		goalPosX = 0.0f;
		goalPosY = 0.0f;
		
		drawingRect = false;
		
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			if(angle < 70)
			{
				angle += 60.0f * deltaTime;
			}
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			if(angle > -70)
			{
				angle -= 60.0f * deltaTime;
			}
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.Z))
		{
			if (ballOutOfBounds() || (ballPosX == 0.0f && ballPosY == 0.0f))
			{
				xAngle = (-angle);
				yAngle = (90.0f - Math.abs(angle));

				ballPosX = (xAngle / 90.0f) * 20.0f;
				ballPosY = (yAngle / 90.0f) * 30.0f;
				move_x = xAngle * 5 * deltaTime;
				move_y = yAngle * 5 * deltaTime;
			}
			zPressed = true;
		}
		
		if (zPressed == true)
		{
			for(int i = 0; i < rectangles.size(); i++)
			{
				/* The x1 and x2 variables are not necessarily left and right, instead
				 * they depend on whether the user drew a rectangle from left to right
				 * or right to left. The same can be said for the y1 and y2 variables
				 * not necessarily being bottom and top.
				 * Because of this we take the minimum and maximum values of these variables
				 * as seen in the code here below.*/
				float left 	 = Math.min(rectangles.get(i).getX1(), rectangles.get(i).getX2());
				float right  = Math.max(rectangles.get(i).getX1(), rectangles.get(i).getX2());
				float bottom = Math.min(rectangles.get(i).getY1(), rectangles.get(i).getY2());
				float top 	 = Math.max(rectangles.get(i).getY1(), rectangles.get(i).getY2());
				if (ballPosY >= bottom && ballPosY <= top && ballPosX >= left && ballPosX <= right)
				{
					if(ballPosY - move_y < bottom )
					{
						move_y *= -1;
					}
					else if(ballPosY - move_y > top)
					{
						move_y *= -1;
					}
					/* Our y coordinates are from 0 to the height of the screen
					 * but our x coordinates have the value 0 at the bottom middle
					 * of our screen, thus being negative left of the middle and
					 * positive to the right of it. Because of this we take the absolute
					 * value of the x position of our ball when deciding whether or not
					 * we need to bounce in the opposite direction.*/
					else if(Math.abs(ballPosX - move_x) < Math.abs(left))
					{
						move_x *= -1;
					}
					else if(Math.abs(ballPosX - move_x) > Math.abs(right))
					{
						move_x *= -1;
					}
				}
			}
			ballPosX += move_x;
			ballPosY += move_y;
		}
		
		for (int i = 0; i < lines.size(); i++)
		{
			if (collision(lines.get(i)))
			{
				changeBallDirection(lines.get(i));
			}
		}
		
		/* If our ball goes out of bounds or it reaches the goal
		 * we put it back into it's initial position and clear all
		 * obstacles the user put in.*/
		if (ballOutOfBounds() || goalReached())
		{
			ballPosX = 0.0f;
			ballPosY = 0.0f;
			move_x = 0.0f;
			move_y = 0.0f;
			clearObstacles();
		}
	}
	
	private void display()
	{
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		modelMatrix.loadIdentityMatrix();
		/* We make this translation because we wanted the x
		 * coordinates to be 0 in the middle of the screen */
		modelMatrix.addTranslation(512.0f, 0, 0);

		//Drawing the ball
		Gdx.gl.glUniform4f(colorLoc, 0.7f, 0.2f, 0.4f, 1);
		modelMatrix.addTranslation(ballPosX, ballPosY, 0);
		modelMatrix.setShaderMatrix(modelMatrixLoc);
		CircleGraphic.drawSolidCircle();
		
		modelMatrix.loadIdentityMatrix();
		modelMatrix.addTranslation(512.0f, 0, 0);
		
		//Drawing the cannon
		Gdx.gl.glUniform4f(colorLoc, 0, 0, 0, 0);
		modelMatrix.addRotationZ(angle);
		modelMatrix.setShaderMatrix(modelMatrixLoc);
		CannonGraphic.drawCannon();		
		
		/* Adding the obstacles that are always on the screen.
		 * These are the obstacles our user has to avoid in order
		 * to reach the goal.
		 * */
		if (rectangles.isEmpty())
		{
			RectangleGraphic rect = new RectangleGraphic(250.0f, 150.0f, 350.0f, 550.0f, positionLoc);
			rectangles.add(rect);
		}
		if (rectangles.size() == 1)
		{
			RectangleGraphic rect2 = new RectangleGraphic(-500.0f, 400.0f, -200.0f, 600.0f, positionLoc);
			rectangles.add(rect2);
		}
		if (lines.isEmpty())
		{
			Line line = new Line(-200.0f, 200.0f, 200.0f, 200.0f, positionLoc);
			lines.add(line);
		}
		
		/* Drawing the user's rectangles */
		Gdx.gl.glUniform4f(colorLoc, 0.5f, 0.3f, 0.6f, 1);	
		for (int i = 0; i < rectangles.size(); i++) 
		{
			modelMatrix.loadIdentityMatrix();
			modelMatrix.addTranslation(512.0f, 0, 0);
			float x = (rectangles.get(i).getX2() + rectangles.get(i).getX1()) / 2.0f;
			float y = (rectangles.get(i).getY2() + rectangles.get(i).getY1()) / 2.0f;
			modelMatrix.addTranslation(x, y, 0);
			modelMatrix.setShaderMatrix(modelMatrixLoc);
			rectangles.get(i).drawSolidSquare();
		}
		
		/* Drawing the user's lines */
		Gdx.gl.glUniform4f(colorLoc, 0.4f, 0.6f, 0.9f, 1);
		for (int i = 0; i < lines.size(); i++)
		{
			modelMatrix.loadIdentityMatrix();
			modelMatrix.addTranslation(512.0f, 0, 0);
			modelMatrix.setShaderMatrix(modelMatrixLoc);
			lines.get(i).draw();
		}
		
		/* Draw our goal */
		modelMatrix.loadIdentityMatrix();
		modelMatrix.addTranslation(512.0f, 0, 0);
		modelMatrix.addTranslation(300.0f, 650.0f, 0);
		goalPosX = 300.0f;
		goalPosY = 650.0f;
		Gdx.gl.glUniform4f(colorLoc, 0.3f, 0.8f, 0.3f, 1);
		modelMatrix.setShaderMatrix(modelMatrixLoc);
		GoalGraphic.drawGoal();		
	}

	@Override
	public void render () {
		update();
		display();
		displayTempLine();
		displayTempRect();
	}
	
	/* A function to display a line while we are still drawing it
	 * and have not released the right mouse button.
	 * */
	public void displayTempLine()
	{
		if (drawingLine)
		{
			Gdx.gl.glUniform4f(colorLoc, 0.4f, 0.8f, 0.6f, 1);
			Line tempLine = new Line(xLine1, yLine1, mousePosX, mousePosY, positionLoc);
			modelMatrix.loadIdentityMatrix();
			modelMatrix.addTranslation(512.0f, 0, 0);
			modelMatrix.setShaderMatrix(modelMatrixLoc);
			tempLine.draw();
		}
	}

	/* A function to display a rectangle while we are still drawing it
	 * and have not released the left mouse button.
	 * */
	public void displayTempRect()
	{
		if(drawingRect)
		{
			Gdx.gl.glUniform4f(colorLoc, 0.4f, 0.8f, 0.6f, 1);
			RectangleGraphic tempRect = new RectangleGraphic(
					xRect1, 
					yRect1, 
					mousePosX, 
					mousePosY, 
					positionLoc);
			modelMatrix.loadIdentityMatrix();
			modelMatrix.addTranslation(512.0f, 0, 0);
			float x = (tempRect.getX2() + tempRect.getX1()) / 2.0f;
			float y = (tempRect.getY2() + tempRect.getY1()) / 2.0f;
			modelMatrix.addTranslation(x, y, 0);
			modelMatrix.setShaderMatrix(modelMatrixLoc);
			tempRect.drawSolidSquare();
		}
	}

	/* A collision detection function that the detects the more
	 * complicated diagonal collisions of hitting lines. */
	public boolean collision(Line line)
	{
		Coordinates startingPoint = line.getStartingPoint();
		Coordinates endPoint = line.getEndPoint();
		
		Vector2 segVector = new Vector2(endPoint.getX() - startingPoint.getX(), endPoint.getY() - startingPoint.getY());
		Vector2 circPosVector = new Vector2((ballPosX) - startingPoint.getX(), ballPosY - startingPoint.getY());
		
		Vector2 unSegVector = new Vector2(segVector.x / segVector.len(), segVector.y / segVector.len());
		float projVecLength = circPosVector.dot(unSegVector);
		Coordinates closestPointOnLine;
		
		if (projVecLength < 0)
		{
			closestPointOnLine = startingPoint;
		}
		else if (projVecLength > segVector.len())
		{
			closestPointOnLine = endPoint;
		}
		else
		{
			Vector2 projVector = new Vector2(projVecLength * unSegVector.x, projVecLength * unSegVector.y);
			closestPointOnLine = new Coordinates(projVector.x + startingPoint.getX(), projVector.y + startingPoint.getY());
		}
		
		Vector2 distVector = new Vector2((ballPosX) - closestPointOnLine.getX(), ballPosY - closestPointOnLine.getY());
		
		if (distVector.len() < 10.0f)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/* A function that handles changing the ball's direction
	 * when hitting a line */
	public void changeBallDirection(Line line)
	{
		Vector2 parallelVec = new Vector2(line.getEndPoint().getX() - line.getStartingPoint().getX(), line.getEndPoint().getY() - line.getStartingPoint().getY());
		Vector2 normal = new Vector2(- parallelVec.x, - parallelVec.y);
		
		float normalLength = normal.len();
		normal.x /= normalLength;
		normal.y /= normalLength;
		
		float normDist = move_x * normal.x + move_y * normal.y;
		move_x -= 2.0 * normDist * normal.x;
		move_y -= 2.0 * normDist * normal.y;
		move_x = -move_x;
		move_y = -move_y;
	}
	
	/* This function gets called when the ball collides with the goal*/
	public boolean goalReached()
	{
		float distanceX = goalPosX - ballPosX;
		float distanceY = goalPosY - ballPosY;
		float sqDistance = (distanceX * distanceX) + (distanceY * distanceY);
		
		if (sqDistance <= 3600)	//The goal's radius is 50. The radius of the ball is 10
		{						// 50+10 = 60, 60*60 = 3600
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void clearObstacles()
	{
		lines.clear();
		rectangles.clear();
	}
	
	public boolean ballOutOfBounds()
	{
		if ((ballPosX > (Gdx.graphics.getWidth())/2) || (ballPosX < -(Gdx.graphics.getWidth())/2) 
				|| (ballPosY > Gdx.graphics.getHeight()) || ballPosY < 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/* Check whether or not our ball is in it's initial position */
	public boolean initialPosition()
	{
		if (ballPosX == 0.0f && ballPosY == 0.0f && move_x == 0.0f && move_y == 0.0f)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		/* We only want to draw shapes when we the ball is in it's
		 * original position. */
		if(initialPosition())
		{
			/* If button is 1 then we are using the right mouse button
			 * and thus drawing a line. */
			if(button == 1)
			{
				xLine1 = screenX - (Gdx.graphics.getWidth() / 2.0f);
				yLine1 = Gdx.graphics.getHeight() - screenY;
		        drawingLine = true;
			}
			/* If button is 0 then we are using the left mouse button
			 * and thus drawing a rectangle. */
			else if(button == 0)
			{
				xRect1 = screenX - (Gdx.graphics.getWidth() / 2.0f);
				yRect1 = Gdx.graphics.getHeight() - screenY;
				drawingRect = true;
			}
			
			mousePosX = screenX - (Gdx.graphics.getWidth() / 2.0f);
			mousePosY = Gdx.graphics.getHeight() - screenY;
		}
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		/* We only want to draw shapes when we the ball is in it's
		 * original position. */
		if(initialPosition())
		{
			if(button == 1)
			{
				xLine2 = screenX - (Gdx.graphics.getWidth() / 2.0f);
				yLine2 = Gdx.graphics.getHeight() - screenY;
		        drawingLine = false;
				
				Line line = new Line(xLine1, yLine1, xLine2, yLine2, positionLoc);
				lines.add(line);
			}
			else if(button == 0)
			{
				xRect2 = screenX - (Gdx.graphics.getWidth() / 2.0f);
				yRect2 = Gdx.graphics.getHeight() - screenY;
				drawingRect = false;
				
				RectangleGraphic rect = new RectangleGraphic(xRect1, yRect1, xRect2, yRect2, positionLoc);
				rectangles.add(rect);
			}
		}
		return true;
	}
	
	@Override
	public boolean touchDragged(int x, int y, int z)
	{
		/* We only want to draw shapes when we the ball is in it's
		 * original position. */
		if(initialPosition())
		{
			mousePosX = x - (Gdx.graphics.getWidth() / 2.0f);
			mousePosY = Gdx.graphics.getHeight() - y;
		}
		return true;
	}
	
	@Override
	public boolean keyUp(int x) { return false; }
	
	@Override
	public boolean keyDown(int x) { return false; }
	
	@Override
	public boolean mouseMoved(int x, int y) { return false; }
	
	@Override
	public boolean keyTyped(char x) { return false; }
	
	@Override
	public boolean scrolled(int x) { return false; }
}