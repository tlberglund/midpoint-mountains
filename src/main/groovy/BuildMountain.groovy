//
// Midpoint Displacement Mountain Generator
// Copyright © 2010 
// August Technology Group, LLC
// http://augusttechgroup.com
//

import com.augusttechgroup.mountain.Mountain

def scale = 7

m = new Mountain()
scale.times { s ->
  m.grow()
}

m.export()
