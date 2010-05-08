
package com.augusttechgroup.mountain


class Point {
  Point north
  Point east
  Point south
  Point west
  
  Closure displace
  
  double elevation
  int scale
  
  
  def insertSouth(scale, displacer = null) {
    if(south) {
      def north = this
      def south = south
      def middle = new Point(displace: displacer, scale: scale)

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
      def middle = new Point(displace: displacer, scale: scale)

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
  
  def eachEast(Closure closure) {
    def point = this
    while(point) {
      def nextPoint = point.east
      closure.call(point)
      point = nextPoint
    }
  }
  
  def eachSouth(Closure closure) {
    def point = this
    while(point) {
      def nextPoint = point.south
      closure.call(point)
      point = nextPoint
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
  
  
  String toString() {
    elevation?.toString()
  }
  
}

