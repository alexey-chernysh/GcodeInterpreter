package Interpreter.Parser;

import Exceptions.GcodeRuntimeException;
import Interpreter.State.InterpreterState;

public class CNCBlockValueList {
	
	private static final int blockSize_ = ParamList.UNUSED.ordinal();
	private double[] valueList_ = new double[CNCBlockValueList.blockSize_];
	private int L_ = -1;
	private int D_ = -1;
	private int H_ = -1;
	
	public CNCBlockValueList(){
		for(int i=0; i<CNCBlockValueList.blockSize_; i++)
			valueList_[i] = 0.0;
	}
	
	public void setA(double v){
		valueList_[ParamList.A.ordinal()] = v;
	}
	
	public double getA(){
		return valueList_[ParamList.A.ordinal()];
	}

	public void setB(double v){
		valueList_[ParamList.B.ordinal()] = v;
	}
	
	public double getB(){
		return valueList_[ParamList.B.ordinal()];
	}

	public void setC(double v){
		valueList_[ParamList.C.ordinal()] = v;
	}
	
	public double getC(){
		return valueList_[ParamList.C.ordinal()];
	}

	public void setX(double v){
		valueList_[ParamList.X.ordinal()] = v;
	}
	
	public double getX(){
		return valueList_[ParamList.X.ordinal()];
	}

	public void setY(double v){
		valueList_[ParamList.Y.ordinal()] = v;
	}
	
	public double getY(){
		return valueList_[ParamList.Y.ordinal()];
	}

	public void setZ(double v){
		valueList_[ParamList.Z.ordinal()] = v;
	}
	
	public double getZ(){
		return valueList_[ParamList.Z.ordinal()];
	}

	public void setD(double v) throws GcodeRuntimeException{
		this.D_ = checkToolNum(v);
	}
	
	public int getD(){
		return this.D_;
	}
	
	public void setP(double v){
		valueList_[ParamList.P.ordinal()] = v;
	}
	
	public double getP(){
		return valueList_[ParamList.P.ordinal()];
	}
	
	public void setR(double v){
		valueList_[ParamList.R.ordinal()] = v;
	}
	
	public double getR() {
		return valueList_[ParamList.R.ordinal()];
	} 
	
	public int getH() {
		return this.H_;
	}

	public void setH(double v) throws GcodeRuntimeException {
		this.H_ = checkToolNum(v);
	}
	
	public int getL() {
		return this.L_;
	}

	public void setL(double v) throws GcodeRuntimeException {
		this.L_ = checkToolNum(v);
	}
	
	private int checkToolNum(double v) throws GcodeRuntimeException{
		int n = (int)v;
		if( ( (v - n) != 0.0)
			||(n < 0.0)
			||(n > InterpreterState.toolSet_.getMaxToolNum())) 
			throw new GcodeRuntimeException("D should be positive integer number of existing tool!");
		return n;
	}

	public enum ParamList {
		A,
		B,
		C,
		P,
		R,
		X,
		Y,
		Z,
		UNUSED;
	}

}
