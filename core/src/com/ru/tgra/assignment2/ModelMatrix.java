package com.ru.tgra.assignment2;

public class ModelMatrix extends Matrix {
	
	public ModelMatrix()
	{
		super();
	}

	public void addTranslation(float Tx, float Ty, float Tz)
	{
		matrix.put(12, matrix.get(0)*Tx + matrix.get(4)*Ty + matrix.get(8)*Tz + matrix.get(12));
		matrix.put(13, matrix.get(1)*Tx + matrix.get(5)*Ty + matrix.get(9)*Tz + matrix.get(13));
		matrix.put(14, matrix.get(2)*Tx + matrix.get(6)*Ty + matrix.get(10)*Tz + matrix.get(14));
	}
}
