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
import HAL.Command.HALCommand;
import Interpreter.State.InterpreterState;

public class ProgramLoader {

	private String fileName_;
	private static ArrayList<LineLoaderArray> sub_program_list = new ArrayList<LineLoaderArray>();
	public static InterpreterState interpreterState;
	public static ArrayList<HALCommand> hal_commands = new ArrayList<HALCommand>();
	
	public  ProgramLoader(String fn) throws LexerException, GcodeRuntimeException{
		fileName_ = fn;
		interpreterState = new InterpreterState();
		int stringCounter = 0;
		try{
			InputStream f = new FileInputStream(fileName_);

			BufferedReader inputStream = new BufferedReader(new InputStreamReader(f));
			String line;
			int currentSubNumber = 0;
			LineLoaderArray current_program = new LineLoaderArray(currentSubNumber);
			sub_program_list.add(current_program);
			while ((line = inputStream.readLine()) != null) {
				LineLoader currentBlock = new LineLoader(line, currentSubNumber);
				int newSubNum = currentBlock.getProgramNum();
				if(newSubNum == currentSubNumber) { 
					current_program.add(currentBlock); 
				}
				else {
					currentSubNumber = newSubNum;
					current_program = new LineLoaderArray(newSubNum);
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
			LineLoaderArray mainModule = this.sub_program_list.get(0);
			try {
				mainModule.evalute();
			} catch (GcodeRuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
