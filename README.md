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

###Using the Elevation Data
The `BuildMountain.groovy` script illustrates how to run the algorithm and dump the data to stdout, but beyond that, the elevation data is available to you to do with as you please. Generating a random mountain looks like this:

    def m = new Mountain()
    10.times {
      m.grow()
    }
	
After which `m` is available to use as a 2D array of elevation values. `m.size()` returns the number of rows in the array (it would be 1025 in this case), and `m[226][762]` would return the elevation value at the 227th row and the 763rd column.

###Making Animated Mountains
I've provided a somewhat special-purpose script to build an array of animated mountain data. `src/main/groovy/AnimateMountain.groovy` will generate four random landscapes, then create 19 interpolated landscapes in between each, ending up with the first landscape. This creates an interesting, "dancing landscape" effect when the frames are rendered and animated. I wrote the script to support a particular graphic I was trying to create for a presentation, but it's here as a template for further application of the core algorithm.

To run, simply type (after building): 

    groovy -cp build/classes/main/ src/main/groovy/AnimateMountain

You'll get a set of 80 .txt files in the data directory. The scale of each mountain is set by the `scale` variable in the script, just like in `BuildMountain.groovy`.

##Desired Improvements
I'd be thrilled if somebody with some JOGL or Java3D chops would pitch in and add code to render the data graphically. I have no streamlined solution for this at present, but hope to have this fixed in the future. Contributions in this area are, of course, welcome. 