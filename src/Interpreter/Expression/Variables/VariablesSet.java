package Interpreter.Expression.Variables;

import Interpreter.InterpreterException;

public class VariablesSet {
	
	private static final int arraySize_ = 10320;
	private static final int G28HomePos_ = 5160;
	private static final int G30HomePos_ = 5180;
	private static final int ScalePos_ = 5190;
	private static final int G92OffsetPos_ = 5210;
	private static final int workOffsetsPos_ = 5220;
	private static final int shift_ = 20;
	private VarArray va = new VarArray();
	
	public VariablesSet(){
	}
	
	public double get(int num) throws InterpreterException{
		return this.va.get(num);
	}
	
	public void set(int num, double value) throws InterpreterException{
		this.va.set(num, value);
	}
	
	private void setX(int base, double value) throws InterpreterException{
		this.va.set(base+1, value);
	}
	private void setY(int base, double value) throws InterpreterException{
		this.va.set(base+2, value);
	}
	private void setZ(int base, double value) throws InterpreterException{
		this.va.set(base+3, value);
	}
	private void setA(int base, double value) throws InterpreterException{
		this.va.set(base+4, value);
	}
	private void setB(int base, double value) throws InterpreterException{
		this.va.set(base+5, value);
	}
	private void setC(int base, double value) throws InterpreterException{
		this.va.set(base+6, value);
	}
	
	private double getX(int base) throws InterpreterException{
		return this.va.get(base+1);
	}
	private double getY(int base) throws InterpreterException{
		return this.va.get(base+2);
	}
	private double getZ(int base) throws InterpreterException{
		return this.va.get(base+3);
	}
	private double getA(int base) throws InterpreterException{
		return this.va.get(base+4);
	}
	private double getB(int base) throws InterpreterException{
		return this.va.get(base+5);
	}
	private double getC(int base) throws InterpreterException{
		return this.va.get(base+6);
	}

	public void setWorkingToolOffset(int P, double X, double Y, double Z, double A, double B, double C) throws InterpreterException{
		if((P>0)&(P<VariablesSet.arraySize_)){
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
	
	public void setScale(double X, double Y, double Z, double A, double B, double C) throws InterpreterException{
			int varPosition = ScalePos_;
			this.setX(varPosition, X);
			this.setY(varPosition, Y);
			this.setZ(varPosition, Z);
			this.setA(varPosition, A);
			this.setB(varPosition, B);
			this.setC(varPosition, C);
	}
	
	public double getCurrentScaleX() throws InterpreterException{
		return this.getX(ScalePos_);
	}
	public double getCurrentScaleY() throws InterpreterException{
		return this.getY(ScalePos_);
	}
	public double getCurrentScaleZ() throws InterpreterException{
		return this.getZ(ScalePos_);
	}
	
	public void setG92Offset(double X, double Y, double Z, double A, double B, double C) throws InterpreterException{
		int varPosition = G92OffsetPos_;
		this.setX(varPosition, X);
		this.setY(varPosition, Y);
		this.setZ(varPosition, Z);
		this.setA(varPosition, A);
		this.setB(varPosition, B);
		this.setC(varPosition, C);
}

	public void setCurrentWorkOffsetNum(int P) throws InterpreterException{
		this.va.set(VariablesSet.workOffsetsPos_, P);
	}
	
	private int getCurrentWorkOffsetNum() throws InterpreterException{
		return (int)this.va.get(VariablesSet.workOffsetsPos_);
	}
	
	public double getOffsetX(int i) throws InterpreterException{
		if(i>0)	return this.getX(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetX() throws InterpreterException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetX(i);
	}
	
	public double getOffsetY(int i) throws InterpreterException{
		if(i>0)	return this.getY(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetY() throws InterpreterException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetY(i);
	}
	
	public double getOffsetZ(int i) throws InterpreterException{
		if(i>0)	return this.getZ(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetZ() throws InterpreterException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetZ(i);
	}
	
	public double getOffsetA(int i) throws InterpreterException{
		if(i>0)	return this.getA(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetA() throws InterpreterException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetA(i);
	}
	
	public double getOffsetB(int i) throws InterpreterException{
		if(i>0)	return this.getB(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetB() throws InterpreterException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetB(i);
	}
	
	public double getOffsetC(int i) throws InterpreterException{
		if(i>0)	return this.getC(workOffsetsPos_ + (i - 1)*shift_);
		else return 0.0;
	}
	
	public double getWorkOffsetC() throws InterpreterException{
		int i = getCurrentWorkOffsetNum();
		return getOffsetC(i);
	}
	
	public double getRadius(int toolNum){
		// TODO real code needed
		return 0.0;
	}
	
	public boolean IsConstant(int num) throws InterpreterException{
		if(num <= 0) throw new InterpreterException("Reference to non initialized variable");
		else
			if(num == 1) return true;
			else return false;
	}
	
	private class VarArray {
		
		private double[] variables_ = new double[arraySize_];
		private int[] assigment_counter_ = new int[arraySize_];

		public VarArray(){
			for( int i = 0; i<VariablesSet.arraySize_; i++){
				variables_[i] = 0.0;
				assigment_counter_[i] = 0;
			}
		}
		
		private int getAssignmentCounter(int num){
			return this.assigment_counter_[num];
		};
		
		public double get(int num) throws InterpreterException{
			if((num>=0)&(num<VariablesSet.arraySize_)){ 
				if(getAssignmentCounter(num) > 0) return this.variables_[num];
				else throw new InterpreterException("Reference to non initialized variable");
			}
			else throw new InterpreterException("Illegal parameter number");
		}
		
		public void set(int num, double val) throws InterpreterException{
			if((num>=0)&(num<VariablesSet.arraySize_)){ 
				this.variables_[num] = val;
				this.assigment_counter_[num]++;
			}
			else throw new InterpreterException("Illegal parameter number");
		}
		
	}
	
}
