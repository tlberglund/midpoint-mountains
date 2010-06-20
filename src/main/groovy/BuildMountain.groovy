//
// Midpoint Displacement Mountain Generator
// Copyright © 2010 
// August Technology Group, LLC
// http://augusttechgroup.com
//

package com.augusttechgroup.mountain

def scale = 4

m = new Mountain()
m.initialize()
scale.times { m.grow() }

m.export()