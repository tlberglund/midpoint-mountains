#Groovy Midpoint Displacement Mountains

An implementation of the [midpoint-displacement algorithm](http://en.wikipedia.org/wiki/Diamond-square_algorithm#Midpoint_displacement_algorithm) for generating fractal terrain, written in the Groovy language. I made an effort in this version to use functional patterns as much as possible. 

The terrain map is implemented as a 2D array (actually a List under the covers). A previous version represented it as a graph of Point objects, but I judged that to be overkill, since the connections between the points were always strictly rectilinear north/south/east/west edges, and I wasn't storing any important metadata in the points other than their height.
                                                             
##Building
At present, the code builds with Gradle 0.8. Simply run `gradle build` to build.

##Running
As my Gradle-Fu is presently weak, there is no task for running the code from inside the build file. I'd like to do this eventually, but for now, you can run the generator through a couple of provided Groovy scripts, after building the code.

###Making a Single Mountain
To make a single, randomly generated mountain array, run this:   

    groovy -cp build/classes/main/ src/main/groovy/BuildMountain

That will cause a tab-delimited array of elevations to be emitted to stdout. The BuildMountain script defines a variable called `scale`, which sets the size of the array. The elevations will always be a square array 2^scale + 1 elements on a side.
                                        
###Making Animated Mountains
I've provided a somewhat special-purpose script to build an array of animated mountain data. `src/main/groovy/AnimateMountain.groovy` will generate four random landscapes, then create 19 interpolated landscapes in between each, ending up with the first landscape. This creates an interesting, "dancing landscape" effect when the frames are rendered and animated. I wrote the script to support a particular graphic I was trying to create for a presentation, but it's here as a template for further application of the core algorithm.

To run, simply type (after building): 

    groovy -cp build/classes/main/ src/main/groovy/AnimateMountain

You'll get a set of 80 .txt files in the data directory. The scale of each mountain is set by the `scale` variable in the script, just like in `BuildMountain.groovy`.