package Interpreter.Parser;

import CNCExpression.CNCCommandSequence;
import CNCExpression.CNCVarAssignment;
import CNCExpression.CNCWord;
import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;
import Interpreter.CannedCycle.ReturnMode;
import Interpreter.Motion.MotionControlMode;
import Interpreter.Motion.Attributes.DistanceMode;
import Interpreter.Motion.Attributes.Plane;
import Interpreter.Motion.FeedRate.FeedRateMode;
import Interpreter.Spindle.SpindleRotation;
import Interpreter.Tools.ToolHeight.ToolHeightOffset;
import Interpreter.Tools.ToolRadius.Compensation;

public class CNCProgramFrame extends CNCCommandSequence {

	public CNCProgramFrame(String s, int programNumber) throws LexerException, GcodeRuntimeException{
		super(s, programNumber);
	}
	
	public void evalute(CNCProgramModule cncSubProgram) throws GcodeRuntimeException{
		int size = this.commandSet_.size();
		CNCSequenciveBlock evalutionSequenciveBlock = new CNCSequenciveBlock();
		int i;
		for(i=0; i<size; i++){
			CNCWord currentCommand = this.commandSet_.get(i);
			currentCommand.evalute();
			double value = currentCommand.getValue();
			int commandNum = (int) (10*value);
			switch(currentCommand.getType()){
			case F:
				evalutionSequenciveBlock.setFeedRate(value);
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
					evalutionSequenciveBlock.setDwell();
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
					evalutionSequenciveBlock.setPlane(Plane.XY);
					break;
				case 180:
					evalutionSequenciveBlock.setPlane(Plane.XZ);
					break;
				case 190:
					evalutionSequenciveBlock.setPlane(Plane.YZ);
					break;
				case 200:
					evalutionSequenciveBlock.setUnitsImperial();
					break;
				case 210:
					evalutionSequenciveBlock.setUnitsMetric();
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
					evalutionSequenciveBlock.setCompensation(Compensation.OFF);
					break;
				case 410:
					evalutionSequenciveBlock.setCompensation(Compensation.LEFT);
					break;
				case 420:
					evalutionSequenciveBlock.setCompensation(Compensation.RIGHT);
					break;
				case 430:
					evalutionSequenciveBlock.setHeightOffset(ToolHeightOffset.ON);
					break;
				case 490:
					evalutionSequenciveBlock.setHeightOffset(ToolHeightOffset.OFF);
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
					evalutionSequenciveBlock.setFixtureToolOffset(1);
					break;
				case 550:
					evalutionSequenciveBlock.setFixtureToolOffset(2);
					break;
				case 560:
					evalutionSequenciveBlock.setFixtureToolOffset(3);
					break;
				case 570:
					evalutionSequenciveBlock.setFixtureToolOffset(4);
					break;
				case 580:
					evalutionSequenciveBlock.setFixtureToolOffset(5);
					break;
				case 590:
					evalutionSequenciveBlock.setFixtureToolOffset(CNCSequenciveBlock.G59_SELECTED);
					break;
				case 610:
					evalutionSequenciveBlock.setMotionMode(MotionControlMode.EXACT_STOP);
					break;
				case 640:
					evalutionSequenciveBlock.setMotionMode(MotionControlMode.CONTINUOUS_SPEED);
					break;
				case 680:
					break;
				case 690:
					break;
				case 700:
					evalutionSequenciveBlock.setUnitsImperial();
					break;
				case 710:
					evalutionSequenciveBlock.setUnitsMetric();
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
					evalutionSequenciveBlock.setDistanceMode(DistanceMode.ABSOLUTE);
					break;
				case 910:
					evalutionSequenciveBlock.setDistanceMode(DistanceMode.INCREMENTAL);
					break;
				case 920:
					break;
				case 930:
					evalutionSequenciveBlock.setFeedRateMode(FeedRateMode.INVERSE_TIME_FEED_MODE);
					break;
				case 940:
					evalutionSequenciveBlock.setFeedRateMode(FeedRateMode.FEED_PER_MINUTE_MODE);
					break;
				case 950:
					evalutionSequenciveBlock.setFeedRateMode(FeedRateMode.FED_PER_REV_MODE);
					break;
				case 980:
					evalutionSequenciveBlock.setReturnMode(ReturnMode.RETURN_NO_LOWER_THEN_R);
					break;
				case 990:
					evalutionSequenciveBlock.setReturnMode(ReturnMode.RETURN_TO_R);
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
					evalutionSequenciveBlock.setSpindleRotation(SpindleRotation.CLOCKWISE);
					break;
				case 40:
					evalutionSequenciveBlock.setSpindleRotation(SpindleRotation.COUNTERCLOCKWISE);
					break;
				case 50:
					evalutionSequenciveBlock.setSpindleRotation(SpindleRotation.OFF);
					break;
				case 60:
					break;
				case 70:
					evalutionSequenciveBlock.M7();
					break;
				case 80:
					evalutionSequenciveBlock.M8();
					break;
				case 90:
					evalutionSequenciveBlock.M9();
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
				evalutionSequenciveBlock.setSpindelSpeed(value);
				break;
			case T:
				evalutionSequenciveBlock.setTool((int)value);
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
