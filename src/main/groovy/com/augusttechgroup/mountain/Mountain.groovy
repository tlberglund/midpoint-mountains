
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
    scaleFunction = { scale ->
      random.nextGaussian() / Math.pow(2, scale)
    }
    
    def displacer = scaleFunction.curry(scale)
    def range = buildInitialRange(displacer, scale)
    northWest = range[0][0]
  }
  

  def buildInitialRange(displacer = null, scale = 1) {
    def northWest = new Point(displacer: displacer, scale: scale)
    def northEast = new Point(displacer: displacer, scale: scale)
    def southEast = new Point(displacer: displacer, scale: scale)
    def southWest = new Point(displacer: displacer, scale: scale)
    
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

    northWest.eachSouth { westernPoint -> 
      westernPoint.eachEast { point -> 
        point.displace() 
      }
    }
    
    northWest.eachSouth { point -> 
      doInsertionsOnEastWestRow(point, scale, scaleFunction.curry(scale))
    }
    
    northWest.eachSouth { point ->
      def southernPoint = point.south
      if(southernPoint) {
        insertBetweenRows(point, southernPoint, scale, scaleFunction.curry(scale))
      }
    }
  }


  def export() {
    def list = northWest.collectSouth { nsPoint ->
      nsPoint.collectEast { ewPoint -> ewPoint.elevation }
    }
    
    list.each { row ->
      println row.join('\t')
    }
  }


  def doInsertionsOnEastWestRow(westernPoint, scale, displacer = null) {
    westernPoint.eachEast { point ->
      if(point.east) point.insertEast(scale, displacer)
    }
  }
  
  //
  // Fix up north/south links in the newly inserted rows. Sadly, this just can't
  // be done in Point, since point can't know the state of its northern and 
  // southern neighbor rows, and such state may not be established when the
  // Point insert methods are called.
  //
  def maintainNorthSouthLinks(westernPoint) {
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
    
  def insertBetweenRows(northRow, southRow, scale, displacer = null) {
    def northIterator = northRow.eastIterator()
    def southIterator = southRow.eastIterator()
    def lastMiddlePoint
    def firstMiddlePoint
    
    while(northIterator.hasNext()) {
      def northPoint = northIterator?.next()
      def southPoint = southIterator?.next()
      
      def middlePoint = new Point()
      middlePoint.scale = scale
      middlePoint.displacer = displacer
      middlePoint.north = northPoint
      middlePoint.south = southPoint
      middlePoint.west = lastMiddlePoint
      if(lastMiddlePoint) lastMiddlePoint.east = middlePoint
      
      northPoint.south = middlePoint
      southPoint.north = middlePoint
      
      lastMiddlePoint = middlePoint
      if(!firstMiddlePoint) firstMiddlePoint = middlePoint
    }
    
    return firstMiddlePoint
  }
}

