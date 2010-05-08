
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
  
  Mountain() {
    random = new Random()
    random.setSeed(new Date().time)
    
    def scale = 1
    def displacer = this.&gaussian.curry(scale)
    def range = buildInitialRange(displacer, scale)
    northWest = range[0][0]
  }
  
  def iterate(northWest) {
    
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
  
  def doInsertionsOnEastWestRow(westernPoint, scale, displacer = null) {
    def point = westernPoint
    while(point) {
      def nextPoint = point.east
      point.insertEast(scale, displacer)
      point = nextPoint
    }
    
    return westernPoint
  }
  
  def insertNewNorthSouthRow(westernPoint, scale, displacer = null) {
    def point = westernPoint
    while(point) {
      def nextPoint = point.east
      point.insertSouth(scale, displacer)
      point = nextPoint
    }
    
    return westernPoint
  }
  
  
  
  double gaussian(double scale) {
    random.nextGaussian() / scale
  }
      
}

