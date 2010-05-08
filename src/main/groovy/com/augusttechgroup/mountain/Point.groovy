
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
  
  
  
}

