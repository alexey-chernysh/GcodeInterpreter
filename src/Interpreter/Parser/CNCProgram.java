package Interpreter.Parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;

public class CNCProgram {

	private String fileName_;
	public static ArrayList<CNCProgramModule> sub_program_list = new ArrayList<CNCProgramModule>();
	
	public  CNCProgram(String fn) throws LexerException, GcodeRuntimeException{
		fileName_ = fn;
		int stringCounter = 0;
		try{
			InputStream f = new FileInputStream(fileName_);

			BufferedReader inputStream = new BufferedReader(new InputStreamReader(f));
			String line;
			int currentSubNumber = 0;
			CNCProgramModule current_program = new CNCProgramModule(currentSubNumber);
			sub_program_list.add(current_program);
			while ((line = inputStream.readLine()) != null) {
				CNCProgramFrame currentBlock = new CNCProgramFrame(line, currentSubNumber);
				int newSubNum = currentBlock.getProgramNum();
				if(newSubNum == currentSubNumber) { 
					current_program.add(currentBlock); 
				}
				else {
					currentSubNumber = newSubNum;
					current_program = new CNCProgramModule(newSubNum);
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
		// TODO Auto-generated method stub
		
	}

}
