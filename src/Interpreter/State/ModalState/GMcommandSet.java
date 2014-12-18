package Interpreter.State.ModalState;

public enum GMcommandSet {
	G0, // Rapid positioning
	G1, // Linear interpolation
	G2, // Clockwise circular/helical interpolation
	G3, // Counterclockwise circular/Helical interpolation
	G4, // Dwell
	G10, // Coordinate system origin setting
	G12, // Clockwise circular pocket
	G13, // Counterclockwise circular pocket
	G15, // Polar Coordinate moves in G0 and G1
	G16, // Polar Coordinate moves in G0 and G1
	G17, // XY Plane select
	G18, // XZ plane select
	G19, // YZ plane select
	G20, // Inch unit
	G21, // Millimetre unit
	G28, // Return home
	G28_1, // Reference axes
	G30, // Return home
	G31, // Straight probe
	G40, // Cancel cutter radius compensation
	G41, // Start cutter radius compensation left
	G42, // Start cutter radius compensation right
	G43, // Apply tool length offset (plus)
	G49, // Cancel tool length offset
	G50, // Reset all scale factors to 1.0
	G51, // Set axis data input scale factors
	G52, // Temporary coordinate system offsets
	G53, // Move in absolute machine coordinate system
	G54, // Use fixture offset 1
	G55, // Use fixture offset 2
	G56, // Use fixture offset 3
	G57, // Use fixture offset 4
	G58, // Use fixture offset 5
	G59, // Use fixture offset 6 / use general fixture number
	G61, // Exact stop mode
	G64, // Constant Velocity mode
	G68, // Rotate program coordinate system
	G69, // Rotate program coordinate system
	G70, // Inch unit
	G71, // Millimetre unit
	G73, // Canned cycle - peck drilling
	G80, // Cancel motion mode (including canned cycles)
	G81, // Canned cycle - drilling
	G82, // Canned cycle - drilling with dwell
	G83, // Canned cycle - peck drilling
	G84, // Canned cycle - right hand rigid tapping
	G85, // Canned cycle - boring
	G86, // Canned cycle - boring
	G88, // Canned cycle - boring
	G89, // Canned cycle - boring
	G90, // Absolute distance mode
	G91, // Incremental distance mode
	G92, // Offset coordinates and set parameters
	G92_1, // Cancel G92 etc.
	G93, // Inverse time feed mode
	G94, // Feed per minute mode
	G95, // Feed per rev mode
	G98, // Initial level return after canned cycles
	G99; // R-point level return after canned cycles
}
