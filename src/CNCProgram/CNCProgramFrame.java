package CNCProgram;

import CNCExpression.CNCCommandSequence;
import CNCExpression.CNCVarAssignment;
import CNCExpression.CNCWord;
import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;
import Interpreter.Motion.Motion.CoordinateSystem.DistanceMode;
import Interpreter.Motion.Motion.CoordinateSystem.Plane;
import Interpreter.Motion.Motion.FeedRate.FeedRateMode;
import Interpreter.Motion.Motion.FeedRate.MotionControlMode;
import Interpreter.Spindle.SpindleRotation;
import Interpreter.Tools.ToolHeight.ToolHeightOffset;
import Interpreter.Tools.ToolRadius.Compensation;

public class CNCProgramFrame extends CNCCommandSequence {

	public CNCProgramFrame(String s) throws LexerException, GcodeRuntimeException{
		super(s);
	}
	
	public void evalute(CNCProgramModule cncSubProgram) throws GcodeRuntimeException{
		int size = this.commandSet_.size();
		CNCSequenciveBlock executionBlock = new CNCSequenciveBlock();
//		final int execBlockSize = ExecutionEnum.STOP_OR_REPEAT.ordinal()+1;
//		boolean[] flag = new boolean[execBlockSize];
		int i;
//		for(i=0; i<execBlockSize; i++) flag[i] = false;
		for(i=0; i<size; i++){
			CNCWord currentCommand = this.commandSet_.get(i);
			currentCommand.evalute();
			double value = currentCommand.getValue();
			int commandNum = (int) (10*value);
			switch(currentCommand.getType()){
			case F:
				executionBlock.setFeedRate(value);
				break;
			case G:
				switch(commandNum){
				case 0:
					break;
				case 10:
					break;
				case 20:
					break;
				case 30:
					break;
				case 40:
					executionBlock.setDwell();
					break;
				case 100:
					break;
				case 120:
					break;
				case 130:
					break;
				case 150:
					break;
				case 160:
					break;
				case 170:
					executionBlock.setPlane(Plane.PLANE_XY);
					break;
				case 180:
					executionBlock.setPlane(Plane.PLANE_XZ);
					break;
				case 190:
					executionBlock.setPlane(Plane.PLANE_YZ);
					break;
				case 200:
					executionBlock.setUnitsImperial();
					break;
				case 210:
					executionBlock.setUnitsMetric();
					break;
				case 280:
					break;
				case 281:
					break;
				case 300:
					break;
				case 310:
					break;
				case 400:
					executionBlock.setCompensation(Compensation.OFF);
					break;
				case 410:
					executionBlock.setCompensation(Compensation.LEFT);
					break;
				case 420:
					executionBlock.setCompensation(Compensation.RIGHT);
					break;
				case 430:
					executionBlock.setHeightOffset(ToolHeightOffset.ON);
					break;
				case 490:
					executionBlock.setHeightOffset(ToolHeightOffset.OFF);
					break;
				case 500:
					break;
				case 510:
					break;
				case 520:
					break;
				case 530:
					break;
				case 540:
					executionBlock.setFixtureToolOffset(1);
					break;
				case 550:
					executionBlock.setFixtureToolOffset(2);
					break;
				case 560:
					executionBlock.setFixtureToolOffset(3);
					break;
				case 570:
					executionBlock.setFixtureToolOffset(4);
					break;
				case 580:
					executionBlock.setFixtureToolOffset(5);
					break;
				case 590:
					executionBlock.setFixtureToolOffset(CNCSequenciveBlock.G59_SELECTED);
					break;
				case 610:
					executionBlock.setMotionMode(MotionControlMode.EXACT_STOP);
					break;
				case 640:
					executionBlock.setMotionMode(MotionControlMode.CONTINUOUS_SPEED);
					break;
				case 680:
					break;
				case 690:
					break;
				case 700:
					executionBlock.setUnitsImperial();
					break;
				case 710:
					executionBlock.setUnitsMetric();
					break;
				case 730:
					break;
				case 800:
					break;
				case 810:
					break;
				case 820:
					break;
				case 830:
					break;
				case 840:
					break;
				case 850:
					break;
				case 860:
					break;
				case 870:
					break;
				case 880:
					break;
				case 890:
					break;
				case 900:
					executionBlock.setDistanceMode(DistanceMode.ABSOLUTE);
					break;
				case 910:
					executionBlock.setDistanceMode(DistanceMode.RELATIVE);
					break;
				case 920:
					break;
				case 930:
					executionBlock.setFeedRateMode(FeedRateMode.INVERSE_TIME_FEED_MODE);
					break;
				case 940:
					executionBlock.setFeedRateMode(FeedRateMode.FEED_PER_MINUTE_MODE);
					break;
				case 950:
					executionBlock.setFeedRateMode(FeedRateMode.FED_PER_REV_MODE);
					break;
				case 980:
					executionBlock.setReturnMode(ReturnMode.RETURN_NO_LOWER_THEN_R);
					break;
				case 990:
					executionBlock.setReturnMode(ReturnMode.RETURN_TO_R);
					break;
				default:
					throw new GcodeRuntimeException("Unsupported G code num");
				}
				break;
			case M:
				switch(commandNum){
				case 0:
					break;
				case 10:
					break;
				case 20:
					break;
				case 30:
					executionBlock.setSpindleRotation(SpindleRotation.CLOCKWISE);
					break;
				case 40:
					executionBlock.setSpindleRotation(SpindleRotation.COUNTERCLOCKWISE);
					break;
				case 50:
					executionBlock.setSpindleRotation(SpindleRotation.OFF);
					break;
				case 60:
					break;
				case 70:
					executionBlock.M7();
					break;
				case 80:
					executionBlock.M8();
					break;
				case 90:
					executionBlock.M9();
					break;
				case 300:
					break;
				case 470:
					break;
				case 480:
					break;
				case 490:
					break;
				case 980:
					break;
				case 990:
					break;
				default:
					throw new GcodeRuntimeException("Unsupported M code num");
				};
				break;
			case S:
				executionBlock.setSpindelSpeed(value);
				break;
			case T:
				executionBlock.setTool((int)value);
				break;
			default:
				throw new GcodeRuntimeException("Unsupported command num");
			}
		}
		size = this.wordList_.getLength();
		for(i=0; i<size; i++){
			CNCWord currentWord = this.wordList_.get(i);
			if(currentWord != null) currentWord.evalute();
		}
		size = this.varAssignmentSet_.size();
		for(i=0; i<size; i++){
			CNCVarAssignment currentVar = this.varAssignmentSet_.get(i);
			currentVar.evalute();
		}
	}
	
}
