package Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;
import Interpreter.State.InterpreterState;

public class ProgramParser {

	private String fileName_;
	private static ArrayList<RoutineParser> sub_program_list = new ArrayList<RoutineParser>();
	private static InterpreterState interpreterState = new InterpreterState();
	
	public  ProgramParser(String fn) throws LexerException, GcodeRuntimeException{
		fileName_ = fn;
		int stringCounter = 0;
		try{
			InputStream f = new FileInputStream(fileName_);

			BufferedReader inputStream = new BufferedReader(new InputStreamReader(f));
			String line;
			int currentSubNumber = 0;
			RoutineParser current_program = new RoutineParser(currentSubNumber);
			sub_program_list.add(current_program);
			while ((line = inputStream.readLine()) != null) {
				LineParser currentBlock = new LineParser(line, currentSubNumber);
				int newSubNum = currentBlock.getProgramNum();
				if(newSubNum == currentSubNumber) { 
					current_program.add(currentBlock); 
				}
				else {
					currentSubNumber = newSubNum;
					current_program = new RoutineParser(newSubNum);
					sub_program_list.add(current_program);
					current_program.add(currentBlock); 
				}
				System.out.println(line);
				stringCounter++;
			}
			inputStream.close();
			f.close();
			this.evalute();
		}
		catch (FileNotFoundException e){
		}
		catch (IOException e){
		}
	}

	private void evalute() {
		
		if(this.sub_program_list.size() > 0){
			// execute "main"/first/number 0 program only
			RoutineParser mainModule = this.sub_program_list.get(0);
			try {
				mainModule.evalute();
			} catch (GcodeRuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}