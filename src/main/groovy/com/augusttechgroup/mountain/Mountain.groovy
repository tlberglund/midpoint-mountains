
package com.augusttechgroup.mountain


/**
 A1 B1
 C1 D1

 A1  AB2 B1
 AC2 BC3 BD2
 C1  CD2 D1

 A1   AAB3  AB2 ABB3  B1
 AAC3 AABACBC5 ABCD3
 AC2  ACBC3 BC3 BCBD3 BD2
 C1   CCD3  CD2  CDD3 D1

 X X
 X X
 
 X Y X
 Y Y Y
 X Y X

 X Y X Y X
 Y Y Y Y Y
 X Y X Y X
 Y Y Y Y Y
 X Y X Y X
 
 
        north
 west         east
       south
 
 elevation

 **/
class Mountain {

  Random random
  Point northWest
  Closure scaleFunction
  int scale
  
  Mountain() {
    random = new Random()
    random.setSeed(new Date().time)
    
    scale = 1
    scaleFunction = { -> scale
      random.nextGaussian() / Math.pow(2, scale)
    }
    
    def displacer = this.&gaussian.curry(scale)
    def range = buildInitialRange(displacer, scale)
    northWest = range[0][0]
  }
  

  def buildInitialRange(displacer = null, scale = 1) {
    def northWest = new Point(displace: displacer, scale: scale)
    def northEast = new Point(displace: displacer, scale: scale)
    def southEast = new Point(displace: displacer, scale: scale)
    def southWest = new Point(displace: displacer, scale: scale)
    
    northWest.east = northEast
    northWest.south = southWest
    
    northEast.west = northWest
    northEast.south = southEast
    
    southEast.north = northEast
    southEast.west = southWest
    
    southWest.north = northWest
    southWest.east = southEast
    
    return [[northWest, northEast], [southWest, southEast]]
  }
  
  def iterate() {
    scale++
    
    northWest.eachSouth { point -> 
      doInsertionsOnEastWestRow(point, scale, scaleFunction.curry(scale))
    }
    
    northWest.eachSouth { point ->
/*      println "INSERTING NEW ROW FOR WEST ${point}"*/
      insertNewNorthSouthRow(point, scale, scaleFunction.curry(scale))
    }
  }

  def export() {
    def list = northWest.collectSouth { nsPoint ->
      nsPoint.collectEast { ewPoint -> ewPoint.elevation }
    }
    
    list.each { row ->
      println row.join(',')
    }
  }

  def doInsertionsOnEastWestRow(westernPoint, scale, displacer = null) {
    westernPoint.eachEast { point ->
      if(point.east) point.insertEast(scale, displacer)
    }
    
    //
    // Fix up north/south links in the newly inserted rows. Sadly, this just can't
    // be done in Point
    //
  }
  
  def maintainNorthSouthLinks(westernPoint) {
    print "NORTH="
    westernPoint.north?.eachEast { println it }
    print "MIDDLE="
    westernPoint.eachEast { println it }
    print "SOUTH="
    westernPoint.south.eachEast { println it }
    def northIterator = westernPoint.north?.eastIterator()
    def middleIterator = westernPoint.eastIterator()
    def southIterator = westernPoint.south?.eastIterator()
    while(middleIterator.hasNext()) {
      def northPoint = northIterator?.next()
      def middlePoint = middleIterator.next()
      def southPoint = southIterator?.next()
      middlePoint.north = northPoint
      middlePoint.south = southPoint
      if(northPoint) northPoint.south = middlePoint
      if(southPoint) southPoint.north = middlePoint
    }
  }
  
  def insertNewNorthSouthRow(westernPoint, scale, displacer = null) {
    westernPoint.eachEast { point ->
/*      println "${point}->${point.south}"*/
      if(point.south) point.insertSouth(scale, displacer)
    }
  }      
}

