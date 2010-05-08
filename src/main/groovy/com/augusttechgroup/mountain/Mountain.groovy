
package com.augusttechgroup.mountain


/**
 A1 B1
 C1 D1

 A1  AB2 B1
 AC2 AC2 BD2
 C1  CD2 D1

 X X X X X
 X X X X X
 X X X X X
 X X X X X
 X X X X X
 
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
    
    def displacer = this.&gaussian
    def range = buildInitialRange(displacer, 1)
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
  
  
  def addMidpoints() {
    def point = northWest

  }
  
  def insertBetweenEastAndWest(point, displacer = null) {
    if(point && point.east) {
      def west = point
      def east = point.east
      def middle = new Point()

      middle.displace = displacer
      middle.west = west
      middle.east = east

      west.east = middle
      east.west = middle

      return [west, middle, east]
    }
    else {
      return null
    }
  }
  
  
  def insertBetweenNorthAndSouth(point, displacer) {
    if(point && point.south) {
      def middle = new Point()
      middle.displacer = displacer
      middle.north = point
      middle.south = point.south
      middle.west = point.west
      middle.east = point.east
      point.east = point
      point.east.west = point
    }
  }
  
  
  
  
  double gaussian(double scale) {
    random.nextGaussian() / scale
  }
      
}

