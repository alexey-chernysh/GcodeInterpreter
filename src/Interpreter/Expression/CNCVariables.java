package Interpreter.Expression;

import Exceptions.GcodeRuntimeException;

public class CNCVariables {
	
	private static final int arraySize_ = 10320;
	private static final int G28HomePos_ = 5160;
	private static final int G30HomePos_ = 5180;
	private static final int ScalePos_ = 5190;
	private static final int G92OffsetPos_ = 5210;
	private static final int workOffsetsPos_ = 5220;
	private static final int shift_ = 20;
	private VarArray va = new VarArray();
	
	public CNCVariables(){
	}
	
	public double get(int num) throws GcodeRuntimeException{
		return this.va.get(num);
	}
	
	public void set(int num, double value) throws GcodeRuntimeException{
		this.va.set(num, value);
	}
	
	private void setX(int base, double value) throws GcodeRuntimeException{
		this.va.set(base+1, value);
	}
	private void setY(int base, double value) throws GcodeRuntimeException{
		this.va.set(base+2, value);
	}
	private void setZ(int base, double value) throws GcodeRuntimeException{
		this.va.set(base+3, value);
	}
	private void setA(int base, double value) throws GcodeRuntimeException{
		this.va.set(base+4, value);
	}
	private void setB(int base, double value) throws GcodeRuntimeException{
		this.va.set(base+5, value);
	}
	private void setC(int base, double value) throws GcodeRuntimeException{
		this.va.set(base+6, value);
	}
	
	private double getX(int base) throws GcodeRuntimeException{
		return this.va.get(base+1);
	}
	private double getY(int base) throws GcodeRuntimeException{
		return this.va.get(base+2);
	}
	private double getZ(int base) throws GcodeRuntimeException{
		return this.va.get(base+3);
	}
	private double getA(int base) throws GcodeRuntimeException{
		return this.va.get(base+4);
	}
	private double getB(int base) throws GcodeRuntimeException{
		return this.va.get(base+5);
	}
	private double getC(int base) throws GcodeRuntimeException{
		return this.va.get(base+6);
	}

	public void setWorkingToolOffset(int P, double X, double Y, double Z, double A, double B, double C) throws GcodeRuntimeException{
		if((P>0)&(P<CNCVariables.arraySize_)){
			int varPosition = workOffsetsPos_ + (P-1)*shift_;
			this.setX(varPosition, X);
			this.setY(varPosition, Y);
			this.setZ(varPosition, Z);
			this.setA(varPosition, A);
			this.setB(varPosition, B);
			this.setC(varPosition, C);
			this.setCurrentWorkOffsetNum(P);
		}
	}
	
	public void setScale(double X, double Y, double Z, double A, double B, double C) throws GcodeRuntimeException{
			int varPosition = ScalePos_;
			this.setX(varPosition, X);
			this.setY(varPosition, Y);
			this.setZ(varPosition, Z);
			this.setA(varPosition, A);
			this.setB(varPosition, B);
			this.setC(varPosition, C);
	}
	
	public double getCurrentScaleX() throws GcodeRuntimeException{
		return this.getX(ScalePos_);
	}
	public double getCurrentScaleY() throws GcodeRuntimeException{
		return this.getY(ScalePos_);
	}
	public double getCurrentScaleZ() throws GcodeRuntimeException{
		return this.getZ(ScalePos_);
	}
	
	public void setG92Offset(double X, double Y, double Z, double A, double B, double C) throws GcodeRuntimeException{
		int varPosition = G92OffsetPos_;
		this.setX(varPosition, X);
		this.setY(varPosition, Y);
		this.setZ(varPosition, Z);
		this.setA(varPosition, A);
		this.setB(varPosition, B);
		this.setC(varPosition, C);
}

	public void setCurrentWorkOffsetNum(int P) throws GcodeRuntimeException{
		this.va.set(CNCVariables.workOffsetsPos_, P);
	}
	
	private int getCurrentWorkOffsetNum() throws GcodeRuntimeException{
		return (int)this.va.get(CNCVariables.workOffsetsPos_);
	}
	
	public double getOffsetX(int i) throws GcodeRuntimeException{
		if(i>0)	return this.getX(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetX() throws GcodeRuntimeException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetX(i);
	}
	
	public double getOffsetY(int i) throws GcodeRuntimeException{
		if(i>0)	return this.getY(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetY() throws GcodeRuntimeException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetY(i);
	}
	
	public double getOffsetZ(int i) throws GcodeRuntimeException{
		if(i>0)	return this.getZ(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetZ() throws GcodeRuntimeException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetZ(i);
	}
	
	public double getOffsetA(int i) throws GcodeRuntimeException{
		if(i>0)	return this.getA(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetA() throws GcodeRuntimeException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetA(i);
	}
	
	public double getOffsetB(int i) throws GcodeRuntimeException{
		if(i>0)	return this.getB(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetB() throws GcodeRuntimeException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetB(i);
	}
	
	public double getOffsetC(int i) throws GcodeRuntimeException{
		if(i>0)	return this.getC(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetC() throws GcodeRuntimeException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetC(i);
	}
	
	public double getRadius(int toolNum){
		// TODO real code needed
		return 0.0;
	}
	
	public boolean IsConstant(int num) throws GcodeRuntimeException{
		if(num <= 0) throw new GcodeRuntimeException("Reference to non initialized variable");
		else
			if(num == 1) return true;
			else return false;
	}
	
	private class VarArray {
		
		private double[] variables_ = new double[arraySize_];
		private int[] assigment_counter_ = new int[arraySize_];

		public VarArray(){
			for( int i = 0; i<CNCVariables.arraySize_; i++){
				variables_[i] = 0.0;
				assigment_counter_[i] = 0;
			}
		}
		
		public int getAssignmentCounter(int num){
			return this.assigment_counter_[num];
		};
		
		public double get(int num) throws GcodeRuntimeException{
			if((num>=0)&(num<CNCVariables.arraySize_)){ 
				if(this.assigment_counter_[num]>0) return this.variables_[num];
				else throw new GcodeRuntimeException("Reference to non initialized variable");
			}
			else throw new GcodeRuntimeException("Illegal parameter number");
		}
		
		public void set(int num, double val) throws GcodeRuntimeException{
			if((num>=0)&(num<CNCVariables.arraySize_)){ 
				this.variables_[num] = val;
				this.assigment_counter_[num]++;
			}
			else throw new GcodeRuntimeException("Illegal parameter number");
		}
		
	}
	
}
