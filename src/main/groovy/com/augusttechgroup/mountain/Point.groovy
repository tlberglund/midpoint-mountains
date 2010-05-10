
package com.augusttechgroup.mountain


class Point {
  Point north
  Point east
  Point south
  Point west
  
  Closure displacer
  
  double elevation
  int scale
  
  
  def insertSouth(scale, displacer = null) {
    if(south) {
      def north = this
      def south = south
      def middle = new Point(displacer: displacer, scale: scale)

      middle.north = north
      middle.south = south
      north.south = middle
      south.north = middle
      
      return middle
    }
    else {
      return null
    }
  }
  
  def insertEast(scale, displacer = null) {
    if(east) {
      def west = this
      def east = east
      def middle = new Point(displacer: displacer, scale: scale)

      middle.west = west
      middle.east = east
      west.east = middle
      east.west = middle

      return middle
    }
    else {
      return null
    }
  }
  
  Iterator<Point> eastIterator() {
    new PointEastIterator(this)
  }
    
  Iterator<Point> southIterator() {
    new PointSouthIterator(this)
  }
  
  def eachEast(Closure closure) {
    each(eastIterator(), closure)
  }
  
  def eachSouth(Closure closure) {
    each(southIterator(), closure)
  }
  
  def each(Iterator iter, Closure closure) {
    while(iter.hasNext()) {
      closure.call(iter.next())
    }
  }
  
  def collectEast(Closure closure) {
    def list = []
    eachEast { point -> list << closure.call(point) }
    return list
  }
  
  def collectSouth(Closure closure) {
    def list = []
    eachSouth { point -> list << closure.call(point) }
    return list
  }
  
  def displace(initial = 0.0) {
    elevation = initial + displacer()
  }
}

