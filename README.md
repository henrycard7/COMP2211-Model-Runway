# Runway Tool
### Contributors
Lockwood, HarryD (hdl1g21@soton.ac.uk)

Severn, Joshua (js3g21@soton.ac.uk)

Martin, JamesD (jdm1g21@soton.ac.uk)

Card, Henry (hc3g21@soton.ac.uk)

Ren, Haotian (hr4g21@soton.ac.uk)

Varshney, Ayush (av3g21@soton.ac.uk)

## Version History
04/05/2023 - v1.0 Increment 3
23/03/2023 - v0.2 Increment 2
08/03/2023 - v0.1 Increment 1
## Installation
Ensure that the latest version of Java 19 is installed: [Download Java](https://www.oracle.com/uk/java/technologies/downloads/)

## Usage

### Setup
* Extract the source code folder
* Run the executable jar file

### Input Parameters
* Switch to the "Input" tab on the sidebar
* Select the runway you wish to edit
* Type values into the text boxes for designators, length and RESA
* Enter values into the table for TORA, TODA, ASDA, LDA and any Displaced Threshold
* Select "Apply"

### Input Obstacles
* Switch to the "Obstacle" tab on the sidebar
* Select "Add Obstacle"
* Type values into the text boxes for distances, height, width, length and blast allowance
* Select "Apply"
* With valid airport and obstacle parameters at the same time, parameter recalculations should be displayed in the Runway View and the Calculations Table

### Input XML
* Switch to the "XML" tab on the sidebar
* Select "Airport XML"
* Navigate to the XML file and press "Open"

### Using Input Presets
* Switch to the "Presets" tab on the sidebar
* Select the desired preset from the list of Airport Presets
* Parameters for the preset runways should be copied to the "Input" tab and automatically applied
* To add a preset obstacle, select one from the Obstacle Presets list
* Parameters for the preset obstacle should be copied to the "Obstacle" tab and automatically applied

### Switching Directions
* Select the Direction Arrow in the top-left corner to toggle the direction of aircraft travel
* Click and drag the Rotation Slider

### Switching Views
* Select the "View" buttons on the right to toggle between Top-Down and Side-On views
* Use the "3D View" button to open the 3D view, click and drag to move the view around


## Glossary of Terms
**Runway** – this is formally a defined rectangular area on a land aerodrome prepared for the landing and take-off of aircraft.

A runway is named with a number from 01 to 36, which is based on the degree of the runway’s heading direction. For example, runway 09 points to the East (90 degrees to the North) whereas runway 27 points to the West (270 degrees to the North). Normally, a physical runway can be used in both directions, creating two logical runways with a difference in 18 points (180 degrees). In the case of parallel runways, a character is appended to the names of the runways to identify its position. The characters can be L (Left), R (Right), or C (Centre). In the example above, 09 will become 09L and its reciprocal runway will be 27R. Another physical runway will have 09R and 27L as its logical runways. In the case of a single runway, no character is required.

For each runway, there are four published parameters that are affected during the re-declaration process, which are listed and explained below:

**Take-Off Run Available (TORA)** - the length of the runway available for take-off. This is the total distance from the point where an aircraft can commence its take-off to the point where the runway can no longer support the weight of the aircraft under normal conditions (and where it should have left the runway during a normal take-off).

**Take-Off Distance Available (TODA)** - the length of the runway (TORA) plus any Clearway (area beyond the runway that is considered free from obstructions). This is the total distance an aircraft can safely utilise for its take-off and initial ascent.

**Accelerate-Stop Distance Available (ASDA)** - the length of the runway (TORA) plus any Stopway (area that is not part of the TORA, but that can be used to safely stop an aircraft in an emergency). This is the total distance available to the aircraft in case of an aborted take-off.

**Landing Distance Available (LDA)** - the length of the runway available for landing. The start of this is called the threshold and is typically the same as the start of the TORA. A threshold may be displaced for operational reasons or because of a temporary obstacle, in which case LDA and TORA can differ.

Other important terms used here and in the remainder of the specification are listed in the following:

**Displaced Threshold** - A runway threshold located at a point other than the physical beginning or the end of the runway. The displaced portion can be used for take-off but not for landing. A landing aircraft can use the displaced area on the opposite end of the runway.

**Clearway** - An area beyond the end of the TORA, which may be used during an aircraft’s initial climb to a specified height.

**Stopway** - An area beyond the end of the TORA, which may be used in the case of an abandoned take-off so that an aircraft can be safely brought to a stop.

**Runway End Safety Area (RESA)** - An area symmetrical about the extended runway centreline and adjacent to the end of the strip primarily intended to reduce the risk of damage to an aircraft undershooting or overrunning the runway.

**Strip end** - An area between the end of the runway and the end of the runway strip.

**Blast Protection** - A safety area behind an aircraft to prevent the engine blast from affecting any obstacles located behind it.

**Approach Landing Surface (ALS)** - The surface formed between the top of the obstacle and the runway when taking into account the airport’s minimum angle of descent.

**Take-Off Climb Surface (TOCS)** - The surface formed between the runway and the top of the obstacle when taking into account the airport’s minimum angle of ascent.
Runway Strip - An area enclosing a runway and any associated stopway. Its purpose is to reduce the risk of damage to an aeroplane running off the runway and also to protect aeroplanes flying over it during landing, balked landing or take-off.