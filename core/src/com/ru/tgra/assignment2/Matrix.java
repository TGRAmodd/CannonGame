package com.ru.tgra.assignment2;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.BufferUtils;

public class Matrix {
	
	FloatBuffer matrix;
	
	public Matrix()
	{
		matrix = BufferUtils.newFloatBuffer(16);
	}
	
	public void loadIdentityMatrix()
	{
		matrix.put(0, 1.0f);
		matrix.put(1, 0.0f);
		matrix.put(2, 0.0f);
		matrix.put(3, 0.0f);
		matrix.put(4, 0.0f);
		matrix.put(5, 1.0f);
		matrix.put(6, 0.0f);
		matrix.put(7, 0.0f);
		matrix.put(8, 0.0f);
		matrix.put(9, 0.0f);
		matrix.put(10, 1.0f);
		matrix.put(11, 0.0f);
		matrix.put(12, 0.0f);
		matrix.put(13, 0.0f);
		matrix.put(14, 0.0f);
		matrix.put(15, 1.0f);
	}
	
	public void addTransformation(float [] M2)
	{
		float m0 = matrix.get(0)*M2[0] + matrix.get(4)*M2[1] + matrix.get(8)*M2[2] + matrix.get(12)*M2[3];
		float m1 = matrix.get(1)*M2[0] + matrix.get(5)*M2[1] + matrix.get(9)*M2[2] + matrix.get(13)*M2[3];
		float m2 = matrix.get(2)*M2[0] + matrix.get(6)*M2[1] + matrix.get(10)*M2[2] + matrix.get(14)*M2[3];
		float m3 = matrix.get(3)*M2[0] + matrix.get(7)*M2[1] + matrix.get(11)*M2[2] + matrix.get(15)*M2[3];
		float m4 = matrix.get(0)*M2[4] + matrix.get(4)*M2[5] + matrix.get(8)*M2[6] + matrix.get(12)*M2[7];
		float m5 = matrix.get(1)*M2[4] + matrix.get(5)*M2[5] + matrix.get(9)*M2[6] + matrix.get(13)*M2[7];
		float m6 = matrix.get(2)*M2[4] + matrix.get(6)*M2[5] + matrix.get(10)*M2[6] + matrix.get(14)*M2[7];
		float m7 = matrix.get(3)*M2[4] + matrix.get(7)*M2[5] + matrix.get(11)*M2[6] + matrix.get(15)*M2[7];
		float m8 = matrix.get(0)*M2[8] + matrix.get(4)*M2[9] + matrix.get(8)*M2[10] + matrix.get(12)*M2[11];
		float m9 = matrix.get(1)*M2[8] + matrix.get(5)*M2[9] + matrix.get(9)*M2[10] + matrix.get(13)*M2[11];
		float m10 = matrix.get(2)*M2[8] + matrix.get(6)*M2[9] + matrix.get(10)*M2[10] + matrix.get(14)*M2[11];
		float m11 = matrix.get(3)*M2[8] + matrix.get(7)*M2[9] + matrix.get(11)*M2[10] + matrix.get(15)*M2[11];
		float m12 = matrix.get(0)*M2[12] + matrix.get(4)*M2[13] + matrix.get(8)*M2[14] + matrix.get(12)*M2[15];
		float m13 = matrix.get(1)*M2[12] + matrix.get(5)*M2[13] + matrix.get(9)*M2[14] + matrix.get(13)*M2[15];
		float m14 = matrix.get(2)*M2[12] + matrix.get(6)*M2[13] + matrix.get(10)*M2[14] + matrix.get(14)*M2[15];
		float m15 = matrix.get(3)*M2[12] + matrix.get(7)*M2[13] + matrix.get(11)*M2[14] + matrix.get(15)*M2[15];
		
		matrix.put(0, m0);
		matrix.put(1, m1);
		matrix.put(2, m2);
		matrix.put(3, m3);
		matrix.put(4, m4);
		matrix.put(5, m5);
		matrix.put(6, m6);
		matrix.put(7, m7);
		matrix.put(8, m8);
		matrix.put(9, m9);
		matrix.put(10, m10);
		matrix.put(11, m11);
		matrix.put(12, m12);
		matrix.put(13, m13);
		matrix.put(14, m14);
		matrix.put(15, m15);
	}
	
	
	
	public void setShaderMatrix(int shaderMatrixPointer)
	{
		Gdx.gl.glUniformMatrix4fv(shaderMatrixPointer, 1, false, matrix);
	}
}
